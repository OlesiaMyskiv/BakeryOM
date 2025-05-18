package OM.Bakery.service;

import OM.Bakery.model.*; // Імпорт Order, OrderItem, User, Assortment, CartItem
// Можливо, вам не потрібен AssortmentRepository та OrderItemRepository тут, якщо ви працюєте через OrderRepository
// import OM.Bakery.repository.AssortmentRepository;
// import OM.Bakery.repository.OrderItemRepository;
import OM.Bakery.repository.OrderRepository; // Імпорт OrderRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Для транзакцій

import java.util.List;
import java.util.Optional; // Можливо знадобиться

// Сервіс для виконання операцій, пов'язаних із замовленнями.

@Service
public class OrderService {

    // Репозиторій для доступу до даних замовлень у базі даних.
    private final OrderRepository orderRepository;


    // Єдиний конструктор для інжекції залежностей (тут - OrderRepository).
    @Autowired // Або через конструктор
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Метод для створення нового замовлення з елементів кошика користувача.
    @Transactional
    public Order createOrder(User user, List<CartItem> cartItems, Order.DeliveryMethod deliveryMethod, String city, String street, String house) {

        // Перевіряємо вхідні дані на null або порожні списки.
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null when creating an order.");
        }
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an order from an empty cart.");
        }

        // Розраховуємо загальну суму замовлення, сумуючи ціни всіх товарів у кошику з урахуванням кількості.
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> {
                    // Перевірка на null, хоча Assortment має бути NOT NULL в CartItem
                    if (item.getAssortment() != null) {
                        return item.getAssortment().getPrice() * item.getQuantity();
                    }
                    return 0.0;
                })
                .sum();

        // Створюємо новий об'єкт Order з отриманими даними.
        Order order = new Order(user, totalAmount, deliveryMethod, city, street, house);

        // Додаємо елементи замовлення
        for (CartItem cartItem : cartItems) {
            double priceAtOrder = cartItem.getAssortment().getPrice(); // Отримуємо ціну з об'єкта Assortment, пов'язаного з CartItem.

            // Створюємо новий об'єкт OrderItem для поточного елемента кошика.
            OrderItem orderItem = new OrderItem(order, cartItem.getAssortment(), cartItem.getQuantity(), priceAtOrder);
            order.addOrderItem(orderItem); // Додаємо до списку в Order (встановлює зворотний зв'язок)
        }

        // Зберігаємо замовлення. CascadeType.ALL на orderItems забезпечить збереження елементів замовлення автоматично
        Order savedOrder = orderRepository.save(order);

        return savedOrder; // Повертаємо збережене замовлення.
    }
}