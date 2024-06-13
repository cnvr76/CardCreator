package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UIUtils{
    private static Dimension size;

    public static JTextField createTextField(String text, boolean editable) {
        JTextField textField = new JTextField(text);

        textField.setEditable(editable);
        textField.setFocusable(false);
        textField.setHorizontalAlignment(JTextField.CENTER);

        return textField;
    }

    public static JButton createButton(String text, ActionListener e){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.addActionListener(e);
        return button;
    }

    public static JLabel createLabel(String text){
        return new JLabel(text);
    }

    public static ImageIcon resize(ImageIcon icon, int w, int h){
        Image image = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static ImageIcon resize(ImageIcon icon, Dimension panelSize){
        int w = panelSize.width;
        int h = panelSize.height;
        double scale = Math.min((double) w / icon.getIconWidth(),
                (double) h / icon.getIconHeight());

        int newWidth = (int) (icon.getIconWidth() * scale);
        int newHeight = (int) (icon.getIconHeight() * scale);

        return resize(icon, newWidth, newHeight);
    }

    public static JPanel createPanel(LayoutManager layout, Dimension size){
        JPanel panel = new JPanel(layout);
        if (size != null) {
            if (size.getHeight() != 0 && size.getWidth() != 0){
                panel.setPreferredSize(size);
            }else{
                panel.setSize(UIUtils.size);
            }
        }
        return panel;
    }
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setLayout(new GridLayout(6, 1, 2, 2));
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        return panel;
    }




    public static JFrame createWindow(String windowName, Dimension dim, LayoutManager layout){
        size = dim;

        JFrame frame = new JFrame();
        frame.setTitle(windowName);
        frame.setSize(size);
        frame.setLayout(layout);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return frame;
    }
}
