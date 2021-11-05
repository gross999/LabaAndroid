package com.example.first;
import java.io.Serializable;

public class Telephone implements Serializable {
    String Manufacturer;    // Производитель
    String Model;           // Модель
    int Length;             // Длина Экрана
    int Width;              // Ширина экрана

    public Telephone(String Manufacturer, String Model, int Length, int Width) {
        this.Manufacturer = Manufacturer;
        this.Model = Model;
        this.Length = Length;
        this.Width = Width;
    }

    @Override
    public String toString(){
        return Manufacturer + " " + Model + " (" + Length + ", " + Width + ")";
    }
}
