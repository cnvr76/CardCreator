package utils;

import models.BusinessCard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.*;
import java.util.Arrays;

public class FileUtils {

    public static void printCard(JPanel cardPanel){
        PrinterJob printer = PrinterJob.getPrinterJob();
        printer.setJobName("Print card");

        PageFormat pageFormat = new PageFormat();
        Paper paper = pageFormat.getPaper();
        paper.setSize(595, 842);

        double margin = 15;
        paper.setImageableArea(margin, margin * 4, paper.getWidth() - 2 * margin,
                paper.getHeight() - 2 * margin);

        pageFormat.setPaper(paper);
        printer.setPrintable(((graphics, pageFormat1, pageIndex) -> {
            if (pageIndex > 0){
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            double widthScale = pageFormat.getImageableWidth() / cardPanel.getWidth() / 2;
            double heightScale = pageFormat.getImageableHeight() / cardPanel.getHeight() / 2;
            double scale = Math.min(widthScale, heightScale);
            g2d.scale(scale, scale);

            int gap = 10;
            for (int col = 0; col < 2; col++) {
                for (int row = 0; row < 4; row++) {
                    double x = col * (cardPanel.getWidth() + gap);
                    double y = row * (cardPanel.getHeight() + gap);
                    g2d.translate(x, y);
                    cardPanel.paint(g2d);
                    g2d.translate(-x, -y);
                }
            }

            return Printable.PAGE_EXISTS;
        }));

        if (printer.printDialog()){
            try{
                printer.print();
            }catch (PrinterException pe){
                JOptionPane.showMessageDialog(null, "Print error: " + pe.getMessage());
            }
        }
    }

    public static void saveCard(BusinessCard card, String filename){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(card);
            System.out.println("Card saved successfully as " + filename);
        } catch (IOException e) {
            System.out.println("Error in saving card: " + e.getMessage());
        }
    }

    private static void savePng(Component panel, File file){
        try {
            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            panel.paint(g);
            g.dispose();
            ImageIO.write(image, "png", file);
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void showSaveDialog(JFrame parentFrame, Component panel){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as...");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image", "png"));

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION){
            File fileToSave = fileChooser.getSelectedFile();
            String ext = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            try{
                savePng(panel, new File(fileToSave.getAbsolutePath() + "." + ext));
                JOptionPane.showMessageDialog(parentFrame, "Card saved successfully!", "Saving",
                        JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                JOptionPane.showMessageDialog(parentFrame, "Error in saving occured!", "Not saving",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static BusinessCard loadCard(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (BusinessCard) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading card: " + e.getMessage());
            return null;
        }
    }


    public static ImageIcon loadImage(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Select an image");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "png", "webp"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            return new ImageIcon(selectedFile.getAbsolutePath());
        }
        return null;
    }
}
