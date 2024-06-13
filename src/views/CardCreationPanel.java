package views;

import models.BusinessCard;
import models.CardComponent;
import utils.DragAndDrop;
import utils.PopUp;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCreationPanel extends JPanel {

//    public final Dimension cardSize = new Dimension(1050/3, 600/3);
    public final Dimension cardSize = new Dimension(344, 208);
    public JPanel card;
    public Map<String, Component> componentMap;
    public Map<String, ImageIcon> images;
    public PopUp popUp;
    private Image bg;
    public static int imageIndex = 0;

    public CardCreationPanel(){
        images = new HashMap<>();
        componentMap = new HashMap<>();
    }

    public void create(){
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.darkGray);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        card.setBackground(Color.WHITE);
        card.setPreferredSize(cardSize);
        card.setMaximumSize(cardSize);
        makePopup(card);

        this.add(card, gbc);
    }

    public void put(Component component, String as) {
        if (card == null) return;
        if (componentMap.containsKey(as)){
            JLabel label = (JLabel) componentMap.get(as);
            JLabel newLabel = (JLabel) component;
            label.setText(newLabel.getText());
            label.setSize(label.getPreferredSize());
        }else{
            componentMap.put(as, component);
            new DragAndDrop(component, card).create();
            card.add(component, 0);
            makePopup(component);
        }
        card.revalidate();
        card.repaint();
    }
    public String put(ImageIcon image, String as) {
        if (card == null) return "";

        String key = String.format("%s%s",as, CardCreationPanel.imageIndex);
        images.put(key, image);
        CardCreationPanel.imageIndex++;

        JLabel imgLabel = new JLabel(image);
        new DragAndDrop(imgLabel, card).create();

        card.add(imgLabel, 0);
//        makePopup(imgLabel);
        card.revalidate();
        card.repaint();
        return key;
    }
    public void rem(String option){
        if (option.equals("Background")){
           this.bg = null;
        }
        if (componentMap.containsKey(option)){
            Component component = componentMap.get(option);
            componentMap.remove(option, component);
            card.remove(component);
        }
        if (images.containsKey(option)){
            JLabel imageLabel = findLabelWithIcon(images.get(option));
            if (imageLabel != null) card.remove(imageLabel);
            images.remove(option, images.get(option));
        }
        card.repaint();
        card.revalidate();
    }

    public void setDefaultStyles(String option){
        if (componentMap.containsKey(option)){
            Component component = componentMap.get(option);
            JLabel label = (JLabel) component;
            label.setFont(null);
        }
        card.repaint();
        card.revalidate();
    }

    public void resize(String dim, String imageKey) {
        // Проверяем формат ввода размеров
        if (!dim.contains("x")) return;
        String[] params = dim.split("x");
        if (params.length < 2) return;

        // Парсинг размеров изображения
        int w, h;
        try {
            w = Integer.parseInt(params[0].trim());
            h = Integer.parseInt(params[1].trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid dimensions format");
            return;
        }

        // Проверка наличия изображения по ключу и его изменение
        if (images.containsKey(imageKey)) {
            ImageIcon image = images.get(imageKey);
            ImageIcon imageResized = UIUtils.resize(image, w, h);

            // Обновление JLabel с существующим ImageIcon
            JLabel labelToUpdate = findLabelWithIcon(image);
            if (labelToUpdate != null) {
                labelToUpdate.setIcon(imageResized);
                labelToUpdate.setBounds(labelToUpdate.getX(), labelToUpdate.getY(),
                        imageResized.getIconWidth(), imageResized.getIconHeight());
            } else {
                // Если соответствующий JLabel не найден, добавляем новый JLabel с изображением
                put(imageResized, imageKey);
            }

            // Обновление карты изображений
            images.put(imageKey, imageResized);

            // Перерисовка и валидация панели
            card.revalidate();
            card.repaint();
        } else {
            System.out.println("No image found for the key: " + imageKey);
        }
    }
    // Метод поиска JLabel с заданным ImageIcon
    private JLabel findLabelWithIcon(ImageIcon targetIcon) {
        for (Component component : card.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                Icon icon = label.getIcon();
                if (icon instanceof ImageIcon && ((ImageIcon) icon).getImage() == targetIcon.getImage()) {
                    return label;
                }
            }
        }
        return null;
    }

    public void setBg(ImageIcon icon){
        this.bg = icon.getImage();
        card.revalidate();
        card.repaint();
    }

    private void makePopup(Component component){
        this.popUp = new PopUp();
        popUp.create();
        popUp.addFor(component);
        JMenuItem changeColorItem = new JMenuItem("Change color");
        changeColorItem.addActionListener(e -> {
            Color color;
            if (component instanceof JLabel){
                color = JColorChooser.showDialog(component, "Choose text color",
                        component.getForeground());
                if (color != null) component.setForeground(color);
            }else{
                color = JColorChooser.showDialog(component, "Choose bg color",
                        component.getBackground());
                if (color != null) component.setBackground(color);
            }
            card.repaint();
            card.revalidate();
        });
        popUp.addItem(changeColorItem);
    }


    public BusinessCard createCardFromPanel(JPanel panel) {
        BusinessCard card = new BusinessCard();

        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel label) {
                ImageIcon icon = (label.getIcon() instanceof ImageIcon) ? (ImageIcon) label.getIcon() : null;
                CardComponent cardComp = new CardComponent(
                        "label",
                        label.getText(),
                        icon,
                        label.getLocation(),
                        comp.getSize(),
                        label.getForeground(),
                        label.getFont()
                );
                card.addComponent(cardComp);
            }
        }
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon image = new ImageIcon("./images/bateman.jpg");
        ImageIcon image2 = new ImageIcon("./images/bateman2.jpeg");
        g.drawImage(image.getImage(), 0, 0, getWidth() * 2, getHeight() / 3 - 5, this);
        g.drawImage(image2.getImage(), 0, 380, getWidth(), getHeight() / 3, this);
    }
}
