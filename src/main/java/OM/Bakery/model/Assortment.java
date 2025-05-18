package OM.Bakery.model;

import jakarta.persistence.*;

@Entity
public class Assortment { // Клас, що представляє одиницю асортименту (товар).

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Вказує, що значення первинного ключа генерується базою даних автоматично (auto-increment).
    private Long id; // Поле для унікального ідентифікатора товару.

    private String name; // Поле для назви товару.

    @Column(columnDefinition = "TEXT") // Вказує, що колонка в базі даних має тип TEXT (для довших рядків).
    private String description; // Поле для опису товару.

    private Double price; // Поле для ціни товару.

    private String imageName; // Поле для назви або шляху до зображення товару.

    // Геттери і сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}