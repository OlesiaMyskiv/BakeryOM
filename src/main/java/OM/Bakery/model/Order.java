package OM.Bakery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // Мапує сутність на таблицю з назвою "orders"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Значення ключа генерується БД автоматично.
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Замовлення належить одному користувачу
    @JoinColumn(name = "user_id", nullable = false) // Колонка для зв'язку з таблицею users
    private User user; // Користувач, який зробив замовлення

    private LocalDateTime orderDate; // Дата та час замовлення

    private double totalAmount; // Загальна сума замовлення

    @Enumerated(EnumType.STRING) // Зберігаємо enum як String в базі
    private DeliveryMethod deliveryMethod; // Спосіб отримання (Самовивіз/Доставка)

    // Поля для адреси доставки (якщо обрана доставка)
    private String city;
    private String street;
    private String house;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // Список товарів у замовленні

    // Enum для способів отримання
    public enum DeliveryMethod {
        PICKUP,
        DELIVERY
    }

    // Конструктори
    public Order() {
        this.orderDate = LocalDateTime.now(); // Встановлюємо поточний час при створенні
    }

    public Order(User user, double totalAmount, DeliveryMethod deliveryMethod, String city, String street, String house) {
        this.user = user;
        this.orderDate = LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.deliveryMethod = deliveryMethod;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Допоміжні методи для додавання OrderItem
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // Встановлюємо зворотний зв'язок
    }

    // Видаляє елемент замовлення зі списку та розриває зворотний зв'язок.
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    @Override
    public String toString() { // Повертає рядкове представлення об'єкта Order.
        return "Order{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") + // Уникаємо зациклення
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", deliveryMethod=" + deliveryMethod +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                // Не включаємо orderItems тут, щоб уникнути зациклення та великого виводу
                '}';
    }
}