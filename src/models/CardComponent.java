package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class CardComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
//    private String option;
    private String type;  // Тип компонента (например, "label", "imageLabel")
    private String text;  // Текст компонента, если это метка
    private ImageIcon image;  // Изображение, если это компонент с изображением
    private Point position;  // Позиция компонента на карточке
    private Dimension size;  // Размер компонента
    private Color color;  // Цвет текста
    private Font font;  // Шрифт текста

    // Конструктор
    public CardComponent(String type, String text, ImageIcon image, Point position, Dimension size,
                         Color color, Font font) {
//        this.option = option;
        this.type = type;
        this.text = text;
        this.image = image;
        this.position = position;
        this.size = size;
        this.color = color;
        this.font = font;
    }
}
