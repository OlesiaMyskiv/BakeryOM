package OM.Bakery.service;

import OM.Bakery.model.Assortment;
import OM.Bakery.model.CartItem;
import OM.Bakery.model.User; // Імпорт User
import OM.Bakery.repository.CartItemRepository; // Імпорт вашого CartItemRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Для транзакцій

import java.util.ArrayList; // Залишено для повернення порожнього списку
import java.util.List;
import java.util.Optional;


@Service
public class CartService { // Сервіс для виконання операцій, пов'язаних з кошиком користувача.

    // Репозиторій для доступу до даних елементів кошика у базі даних.
    private final CartItemRepository cartItemRepository;

    // Єдиний конструктор для інжекції залежностей (тут - CartItemRepository).
    @Autowired
    public CartService(CartItemRepository cartItemRepository ) {
        this.cartItemRepository = cartItemRepository; // Встановлення репозиторію елементів кошика.
    }

    // Метод для додавання товару до кошика користувача.
    @Transactional
    public void addToCart(User user, Assortment assortment) {
        // Перевіряємо, чи користувач або товар не null.
        if (user == null || assortment == null) {
            System.err.println("Cannot add to cart: User or Assortment is null");
            return; // Виходимо з методу, якщо дані некоректні.
        }

        // Шукаємо, чи існує вже елемент кошика для цього користувача та цього товару.
        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserAndAssortment_Id(user, assortment.getId());

        if (cartItemOptional.isPresent()) { // Якщо елемент кошика для цього товару вже існує.
            // Отримуємо існуючий елемент.
            CartItem item = cartItemOptional.get();
            item.setQuantity(item.getQuantity() + 1); // Збільшуємо кількість на 1.
            cartItemRepository.save(item); // Зберігаємо оновлений елемент у базі.
        } else { // Якщо такого товару ще немає в кошику користувача.
            // Створюємо новий елемент кошика з початковою кількістю 1.
            CartItem newCartItem = new CartItem(user, assortment, 1);
            cartItemRepository.save(newCartItem); // Зберігаємо новий елемент у базі.
        }
    }

    // Метод для отримання всіх елементів кошика для конкретного користувача.
    public List<CartItem> getCartItems(User user) {
        // Перевіряємо, чи користувач не null.
        if (user == null) {
            System.err.println("Cannot get cart items: User is null");
            return new ArrayList<>(); // Повертаємо порожній список, якщо користувач null.
        }
        // Викликаємо метод репозиторію для пошуку всіх елементів кошика, пов'язаних з цим користувачем.
        return cartItemRepository.findByUser(user);
    }

    // Метод для розрахунку загальної суми кошика для конкретного користувача.
    public double getTotalPrice(User user) {
        // Перевіряємо, чи користувач не null.
        if (user == null) {
            System.err.println("Cannot get total price: User is null");
            return 0.0; // Повертаємо 0, якщо користувач null.
        }
        // Отримуємо всі елементи кошика для користувача.
        List<CartItem> userCartItems = getCartItems(user);
        // Використовуємо Stream API для обчислення загальної суми.
        return userCartItems.stream()
                .mapToDouble(item -> { // Перетворюємо кожен елемент кошика на його вартість.
                    // Перевірка, що асортимент присутній.
                    if (item.getAssortment() != null) {
                        // Розраховуємо вартість елемента (ціна товару * кількість).
                        return item.getAssortment().getPrice() * item.getQuantity();
                    }
                    return 0.0; // Якщо асортимент null, вартість 0.
                })
                .sum(); // Сумуємо всі вартості елементів, щоб отримати загальну суму.
    }

    // Метод для оновлення кількості певного товару в кошику користувача.
    @Transactional
    public void updateQuantity(User user, Long assortmentId, int quantity) {
        // Перевіряємо вхідні дані.
        if (user == null || assortmentId == null) {
            System.err.println("Cannot update quantity: User or assortmentId is null");
            return;
        }
        // Шукаємо елемент кошика для цього користувача і товару.
        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserAndAssortment_Id(user, assortmentId);

        // Якщо елемент знайдено.
        cartItemOptional.ifPresent(item -> {
            int newQuantity = quantity > 0 ? quantity : 1; // Перевіряємо, що кількість не менше 1.
            if (item.getQuantity() != newQuantity) {
                item.setQuantity(newQuantity);
                cartItemRepository.save(item);
            }
        });
    }

    // Метод для видалення певного товару з кошика користувача.
    @Transactional
    public void removeItem(User user, Long assortmentId) {
        // Перевіряємо вхідні дані.
        if (user == null || assortmentId == null) {
            System.err.println("Cannot remove item: User or assortmentId is null");
            return;
        }
        // Шукаємо елемент кошика для цього користувача і товару.
        Optional<CartItem> cartItemOptional = cartItemRepository.findByUserAndAssortment_Id(user, assortmentId);

        // Якщо елемент знайдено.
        cartItemOptional.ifPresent(item -> {
            cartItemRepository.delete(item);
        });
    }

    // Метод для очищення кошика конкретного користувача (видалення всіх його елементів кошика).
    @Transactional
    public void clearCart(User user) {
        // Перевіряємо, чи користувач не null.
        if (user != null) {
            // Викликаємо кастомний метод репозиторію для видалення всіх елементів за користувачем.
            cartItemRepository.deleteByUser(user);
        } else {
            System.err.println("Cannot clear cart: User is null");
        }
    }
}