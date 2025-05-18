package OM.Bakery.model;

import jakarta.persistence.*; // Переконайтесь, що імпорти JPA присутні

@Entity
@Table(name = "cart_items") // Мапування на вашу таблицю cart_items
public class CartItem { // Клас, що представляє один елемент у кошику користувача.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Встановлює зв'язок "багато до одного" з сутністю Assortment (багато елементів кошика належать одному товару).
    @JoinColumn(name = "assortment_id", nullable = false) // Колонка для зовнішнього ключа
    private Assortment assortment;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY) // Зв'язок з User
    @JoinColumn(name = "user_id", nullable = false) // Колонка для зовнішнього ключа на таблицю users
    private User user; // Користувач, якому належить цей елемент кошика

    // Конструктори
    public CartItem() {
    }

    // Конструктор для створення нового елемента кошика з вказаними користувачем, товаром та кількістю.
    public CartItem(User user, Assortment assortment, int quantity) {
        this.user = user;
        this.assortment = assortment;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assortment getAssortment() {
        return assortment;
    }

    public void setAssortment(Assortment assortment) {
        this.assortment = assortment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Повертає рядкове представлення об'єкта CartItem.
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", assortment=" + (assortment != null ? assortment.getName() : "null") +
                ", quantity=" + quantity +
                ", user=" + (user != null ? user.getEmail() : "null") + // Уникаємо зациклення з User
                '}';
    }
}