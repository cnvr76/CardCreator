package views;

import models.BusinessCard;
import utils.FileUtils;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private JFrame main;
    private ManagementPanel manager;
    private CardCreationPanel drawer;
    private Dimension size = new Dimension(800, 600);

    public MainWindow() {
        this.main = UIUtils.createWindow("Card Creator", size, new GridLayout(1, 2));
        // adding important panels to the window
        main.add(createManagerPanel());
        main.add(createCardCreationPanel());
        main.setResizable(false);

        main.setVisible(true);
    }

    private JPanel createManagerPanel(){
        this.manager = new ManagementPanel();
        manager.create();
        TopMiddlePanel();
        BottomPanel();
        return this.manager;
    }
    private JPanel createCardCreationPanel(){
        this.drawer = new CardCreationPanel();
        drawer.create();
        return this.drawer;
    }

    private void TopMiddlePanel(){
        String[] options = {"Name", "Title", "Company", "Address",
                "Phone", "Email", "Logo", "Image", "Background"};
        JComboBox<String> methodSelector = new JComboBox<>(options);
        methodSelector.addActionListener(e -> {
            String option = (String) methodSelector.getSelectedItem();
            assert option != null;
            if (!option.equals("Image") && !option.equals("Logo") && !option.equals("Background")){
                JTextField textField = new JTextField();
                textField.addActionListener(l -> {
                    String inputText = textField.getText();
                    drawer.put(new JLabel(inputText), option);
                });
                manager.addMultiple("top", new JLabel(option), textField, new JButton("Delete"), drawer);

                String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
                JComboBox<String> fontComboBox = new JComboBox<>(fonts);
                fontComboBox.addActionListener(a -> {
                    String font = (String) fontComboBox.getSelectedItem();
                    JLabel label = (JLabel) drawer.componentMap.get(option);
                    if (label != null){
                        int size = label.getFont().getSize();
                        label.setFont(new Font(font, Font.PLAIN, size));
                    }
                });

                Integer[] sizes = {10, 12, 14, 16, 18, 20, 24, 28, 32, 36, 40};
                JComboBox<Integer> fontSizeComboBox = new JComboBox<>(sizes);
                fontSizeComboBox.addActionListener(a -> {
                    int size = (int) fontSizeComboBox.getSelectedItem();
                    JLabel label = (JLabel) drawer.componentMap.get(option);
                    if (label != null) {
                        String font = label.getFont().getFamily();
                        label.setFont(new Font(font, Font.PLAIN, size));
                        label.setSize(label.getPreferredSize());
                    }
                });

                manager.addMultiple("middle", fontComboBox, fontSizeComboBox,
                        new JButton("Clear for " + option), drawer);


            }else if (option.equals("Background")){
                ImageIcon bg = FileUtils.loadImage();
                if (bg != null){
                    drawer.setBg(bg);
                    manager.addMultiple("top", new JLabel(option), new JLabel(), new JButton("Delete"), drawer);
                }
            }else{
                ImageIcon image = FileUtils.loadImage();
                if (image != null) {
                    image = UIUtils.resize(image, drawer.cardSize);
                    String key = drawer.put(image, option);
                    JLabel imageNameLabel = new JLabel(key);

                    JTextField textField = new JTextField();
                    textField.setText(String.format("%sx%s", image.getIconWidth(), image.getIconHeight()));
                    textField.addActionListener(l -> {
                        String inputText = textField.getText();
                        drawer.resize(inputText, key);
                    });
                    manager.addMultiple("top", imageNameLabel, textField, new JButton("Delete"), drawer);
                }
            }

        });
        manager.addMultiple("top", new JLabel("Options"), methodSelector, null, drawer);
    }

    private void BottomPanel(){
        JButton save = new JButton("Save");
        save.addActionListener(s -> {
            BusinessCard card = drawer.createCardFromPanel(drawer.card);
            System.out.println(card);
            FileUtils.showSaveDialog(main, drawer.card);
            try {
                FileUtils.saveCard(card, manager.enterFilenameWin().concat(".dat"));
            } catch(NullPointerException ex){
                JOptionPane.showMessageDialog(main, "Card saving denied", "Error in saving",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton load = new JButton("Load");
        load.addActionListener(l -> {
            BusinessCard card = FileUtils.loadCard("card.dat");
            System.out.println(card);
//            for (CardComponent comp : card.getComponents()){
//                System.out.println(comp);
//            }
        });
        JButton print = new JButton("Print");
        print.addActionListener(p -> {
            FileUtils.printCard(drawer.card);
        });

        manager.addMultiple("bottom", save, load, null, null);
        manager.addMultiple("bottom", print , new JLabel(), null, null);
    }
}
