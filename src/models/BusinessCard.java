package models;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class BusinessCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<CardComponent> components = new ArrayList<>();

    // Добавление компонента в карточку
    public void addComponent(CardComponent component) {
        components.add(component);
    }
}

