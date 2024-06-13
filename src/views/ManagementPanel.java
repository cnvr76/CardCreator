package views;

import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManagementPanel extends JPanel {
    private JPanel top, middle, bottom;
    private List<String> addedOptions;

    public void create(){
        this.setLayout(new BorderLayout());
        this.addedOptions = new ArrayList<>();
        makePanels();
    }

    private void makePanels(){
        top = UIUtils.createTitledPanel("Add components");
        middle = UIUtils.createTitledPanel("Edit section");
        bottom = UIUtils.createTitledPanel("Save section");

        int gap = 15;
        JSplitPane a = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, middle);
        a.getLeftComponent().setMinimumSize(new Dimension(gap, gap));
        a.getRightComponent().setMinimumSize(new Dimension(gap, gap));
        a.setOneTouchExpandable(true);
        a.setDividerLocation(200);
        JSplitPane b = new JSplitPane(JSplitPane.VERTICAL_SPLIT, a, bottom);
        b.getLeftComponent().setMinimumSize(new Dimension(gap, gap));
        b.getRightComponent().setMinimumSize(new Dimension(gap, gap));
        b.setOneTouchExpandable(true);
        b.setDividerLocation(400);

        this.add(b);
    }

    public void addMultiple(String panel, Component first, Component second, JButton button, CardCreationPanel drawer){
        JPanel row = new JPanel(new GridLayout(1, 3));
        String option;
        if (first instanceof JLabel){
            option = ((JLabel) first).getText();
            if (addedOptions.contains(option)) {
                System.out.println("Already added");
                return;
            }
            addedOptions.add(option);
        } else if (second instanceof JComboBox<?> && first instanceof JComboBox<?>){
            String btnText = button.getText();
            option = btnText.substring(btnText.indexOf("for") + "for".length() + 1).trim();
        }else {
            option = "";
        }
        row.add(first);
        row.add(second);
        if (button != null) {
            row.add(button);
            button.addActionListener(e -> {
                switch (panel) {
                    case "top" -> {
                        drawer.rem(option);
                        top.remove(row);
                    }
                    case "middle" -> {
                        System.out.println(option);
                        drawer.setDefaultStyles(option);
                        middle.remove(row);
                    }
                    case "bottom" -> bottom.remove(row);
                }
                addedOptions.remove(option);
                repaint();
                revalidate();
            });
        }
        switch (panel){
            case "top" -> top.add(row);
            case "middle" -> middle.add(row);
            case "bottom" -> bottom.add(row);
        }
        repaint();
        revalidate();
    }

    public String enterFilenameWin() {
        // Создание диалогового окна
        JDialog dialog = new JDialog((Frame) null, "Enter name of the card", true);
        dialog.setSize(new Dimension(300, 100));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridLayout(2, 1));

        // Текстовое поле для ввода имени файла
        JTextField filenameInput = new JTextField();
        JButton confirm = new JButton("Confirm");

        // Переменная для хранения имени файла
        final String[] filename = new String[1];

        confirm.addActionListener(e -> {
            filename[0] = filenameInput.getText();
            dialog.dispose();
        });

        dialog.add(filenameInput);
        dialog.add(confirm);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        // Возврат имени файла после закрытия диалога
        return filename[0];
    }


}
