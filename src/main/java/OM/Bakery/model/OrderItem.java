package OM.Bakery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items") // Мапує сутність на таблицю з назвою "order_items".
public class OrderItem { // Клас, що представляє один товар у складі замовлення.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Елемент замовлення належить одному замовленню
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Замовлення, до якого належить цей елемент

    @ManyToOne(fetch = FetchType.LAZY) // Елемент замовлення пов'язаний з одним товаром з асортименту
    @JoinColumn(name = "assortment_id", nullable = false)
    private Assortment assortment; // Товар, який було замовлено

    private int quantity; // Кількість цього товару в замовленні

    private double priceAtOrder; // Ціна товару на момент замовлення (щоб уникнути проблем, якщо ціна товару зміниться пізніше)

    // Конструктори
    public OrderItem() {
    }

    // Конструктор для створення нового елемента замовлення.
    public OrderItem(Order order, Assortment assortment, int quantity, double priceAtOrder) {
        this.order = order;
        this.assortment = assortment;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    @Override
    public String toString() { // Повертає рядкове представлення об'єкта OrderItem.
        return "OrderItem{" +
                "id=" + id +
                ", assortment=" + (assortment != null ? assortment.getName() : "null") + // Уникаємо зациклення
                ", quantity=" + quantity +
                ", priceAtOrder=" + priceAtOrder +
                '}';
    }
}