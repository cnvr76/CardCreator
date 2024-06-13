package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopUp {
    private JPopupMenu menu;

    public void create(){
        this.menu = new JPopupMenu();
    }

    public void addFor(Component component){
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger()){
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public void addItem(JMenuItem item){
        menu.add(item);
    }

    public JMenuItem makeItem(String text, ActionListener e){
        JMenuItem item = new JMenuItem();
        item.addActionListener(e);
        return item;
    }

}
