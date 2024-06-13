package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class DragAndDrop extends JPanel{
    private JPanel panel;
    private Component component;

    public DragAndDrop(Component component, JPanel panel){
        this.component = component;
        this.panel = panel;
    }

    public void create() {
        // Инициализация начальных координат компонента
        Point originalLocation = new Point();
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                originalLocation.setLocation(e.getX(), e.getY());
                component.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                component.setCursor(Cursor.getDefaultCursor());
            }
        });

        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Вычисляем новое положение компонента
                int deltaX = e.getX() - originalLocation.x;
                int deltaY = e.getY() - originalLocation.y;

                // Получаем текущие координаты компонента
                Point location = component.getLocation();
                int newX = location.x + deltaX;
                int newY = location.y + deltaY;

                // Проверяем, чтобы компонент не выходил за пределы панели
                if (newX < 0) newX = 0;
                if (newY < 0) newY = 0;
                if (newX + component.getWidth() > panel.getWidth()) newX = panel.getWidth() - component.getWidth();
                if (newY + component.getHeight() > panel.getHeight()) newY = panel.getHeight() - component.getHeight();

                // Устанавливаем новое положение компонента
                component.setLocation(newX, newY);
                panel.repaint();
            }
        });

        // Устанавливаем начальное положение и размер компонента, если это необходимо
        component.setBounds(10, 10, component.getPreferredSize().width + 5,
                component.getPreferredSize().height);

    }
}
