package OM.Bakery.controller;

import OM.Bakery.model.Assortment;
import OM.Bakery.model.CartItem;
import OM.Bakery.model.Order; // Імпорт Order
import OM.Bakery.model.User; // Імпорт User
import OM.Bakery.model.Order.DeliveryMethod; // Імпорт enum DeliveryMethod
import OM.Bakery.service.AssortmentService;
import OM.Bakery.service.CartService; // Імпорт CartService
import OM.Bakery.service.OrderService; // Імпорт OrderService
import OM.Bakery.service.UserService; // Імпорт UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional; // Для транзакції на методі placeOrder
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController { // Контролер для керування кошиком та оформленням замовлень.

    // Інжектуємо сервіси, необхідні для роботи контролера.
    private final CartService cartService;
    private final AssortmentService assortmentService;
    private final UserService userService; // Сервіс для роботи з користувачами.
    private final OrderService orderService; // Сервіс для роботи з замовленнями.

    // Конструктор для інжекції всіх залежностей
    @Autowired
    public CartController(CartService cartService, AssortmentService assortmentService, UserService userService, OrderService orderService) {
        this.cartService = cartService;
        this.assortmentService = assortmentService;
        this.userService = userService;
        this.orderService = orderService;
    }

    // Обробляє GET запит на базовий шлях контролера (/cart).
    @GetMapping
    public String showCart(Model model, Authentication authentication) {  // Метод для відображення сторінки кошика.

        // Отримуємо об'єкт User поточного аутентифікованого користувача.
        User user = getUserByAuthentication(authentication);

        List<CartItem> cartItems = new ArrayList<>(); // Ініціалізуємо список елементів кошика.
        double total = 0.0; // Ініціалізуємо загальну суму.
        boolean isAuthenticated = user != null; // Перевіряємо, чи користувач аутентифікований.

        // Якщо користувач аутентифікований, отримуємо його кошик та суму.
        if (user != null) {
            cartItems = cartService.getCartItems(user); // Отримуємо елементи кошика для користувача.
            total = cartService.getTotalPrice(user); // Розраховуємо загальну суму для користувача.

            model.addAttribute("userFirstName", user.getFirstName());
        }

        // Перевіряємо, чи кошик порожній.
        boolean isCartEmpty = cartItems.isEmpty();

        // Додаємо дані кошика в модель для відображення в представленні.
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("isCartEmpty", isCartEmpty);
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Додаємо дані для навігації/хедера.
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");

        return "cart"; // Повертає назву шаблону "cart" (cart.html).
    }



    // Обробляє POST /cart/add (з форми на сторінці асортименту)
    // Отримуємо ID товару з параметра запиту, oтримуємо об'єкт аутентифікації.
    @PostMapping("/add")
    public String addToCart(@RequestParam Long assortmentId, Authentication authentication, RedirectAttributes redirectAttributes) {

        // Перевіряємо, чи користувач аутентифікований
        User user = getUserByAuthentication(authentication);
        if (user == null) {
            // Якщо користувач не увійшов, перенаправляємо на сторінку входу з повідомленням.
            redirectAttributes.addFlashAttribute("errorMessage", "Будь ласка, увійдіть, щоб додати товари до кошика.");
            return "redirect:/login-form";
        }

        // Шукаємо товар за ID в сервісі асортименту.
        var optionalAssortment = assortmentService.findById(assortmentId);
        if (optionalAssortment.isPresent()) {
            Assortment assortment = optionalAssortment.get();
            cartService.addToCart(user, assortment); // Додаємо товар до кошика користувача через сервіс.
        } else {
            // Якщо товар не знайдено, можна додати повідомлення про помилку
            redirectAttributes.addFlashAttribute("errorMessage", "Товар не знайдено.");
        }
        // Перенаправляємо назад на сторінку асортименту.
        return "redirect:/assortment";
    }

    // Обробляє POST запит на /cart/update (для оновлення кількості товару).
    @PostMapping("/update")
    @ResponseBody
    public String updateCartItemQuantity(@RequestParam Long assortmentId, @RequestParam int quantity, Authentication authentication) {

        // Перевіряємо, чи користувач аутентифікований
        User user = getUserByAuthentication(authentication);
        if (user == null) {
            // Повертаємо помилку для AJAX відповіді, якщо користувач не аутентифікований.
            return "Помилка: Користувач не аутентифікований."; // Приклад простого повідомлення
        }

        // Оновлюємо кількість товару в кошику користувача через сервіс.
        cartService.updateQuantity(user, assortmentId, quantity); // Передаємо User

        // Повертаємо нову загальну суму кошика
        return String.valueOf(cartService.getTotalPrice(user));
    }


    // Обробляє POST запит на /cart/remove (для видалення товару з кошика).
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam Long assortmentId, Authentication authentication, RedirectAttributes redirectAttributes) {

        // Перевіряємо, чи користувач аутентифікований
        User user = getUserByAuthentication(authentication);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Будь ласка, увійдіть, щоб керувати кошиком.");
            return "redirect:/login-form";
        }

        // Видаляємо товар з кошика користувача через сервіс.
        cartService.removeItem(user, assortmentId); // Передаємо User
        return "redirect:/cart"; // Перенаправляємо назад до кошика
    }


    // МЕТОДИ ДЛЯ ОФОРМЛЕННЯ ЗАМОВЛЕННЯ

    // Обробляє POST запит з форми оформлення замовлення на сторінці кошика.
    @PostMapping("/order/place")
    @Transactional // Транзакція, щоб створення замовлення та очищення кошика були атомарними
    public RedirectView placeOrder(@RequestParam("deliveryMethod") String deliveryMethodString, // Отримуємо спосіб доставки.
                                   @RequestParam(value = "city", required = false) String city, // Отримуємо місто (якщо є).
                                   @RequestParam(value = "street", required = false) String street, // Отримуємо вулицю (якщо є).
                                   @RequestParam(value = "house", required = false) String house, // Отримуємо номер будинку (якщо є)
                                   Authentication authentication, // Отримуємо об'єкт аутентифікації
                                   RedirectAttributes redirectAttributes) { // Для передачі даних після редіректу


        // Перевіряємо, що користувач аутентифікований.
        User user = getUserByAuthentication(authentication);
        if (user == null) {
            // Якщо з якихось причин не аутентифікований, перенаправити на вхід або сторінку помилки
            redirectAttributes.addFlashAttribute("errorMessage", "Будь ласка, увійдіть, щоб оформити замовлення.");
            return new RedirectView("/login-form", true); // true - вказує на зовнішній редірект
        }

        // Отримуємо елементи кошика поточного користувача.
        List<CartItem> cartItems = cartService.getCartItems(user); // Отримуємо кошик для КОНКРЕТНОГО користувача

        // Перевіряємо, чи кошик не порожній.
        if (cartItems == null || cartItems.isEmpty()) {
            // Якщо кошик порожній, додаємо повідомлення про помилку та редірект назад до кошика.
            redirectAttributes.addFlashAttribute("errorMessage", "Не можна оформити порожнє замовлення."); // Повідомлення для відображення на сторінці кошика після редіректу
            return new RedirectView("/cart", true);
        }

        // Парсимо спосіб отримання з рядка (приведено до верхнього регістру, щоб відповідало enum)
        DeliveryMethod deliveryMethod;
        try {
            deliveryMethod = DeliveryMethod.valueOf(deliveryMethodString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Некоректний спосіб доставки
            redirectAttributes.addFlashAttribute("errorMessage", "Некоректний спосіб отримання.");
            return new RedirectView("/cart", true);
        }

        // Якщо обрана доставка, перевіряємо наявність адреси (або можна додати більш детальну валідацію)
        if (deliveryMethod == DeliveryMethod.DELIVERY && (city == null || city.trim().isEmpty() || street == null || street.trim().isEmpty() || house == null || house.trim().isEmpty())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Адреса доставки обов'язкова для способу доставки.");
            return new RedirectView("/cart", true);
        }


        try {
            // Створюємо та зберігаємо замовлення через OrderService
            // Передаємо всі потрібні дані
            Order newOrder = orderService.createOrder(user, cartItems, deliveryMethod, city, street, house);

            // Очищаємо кошик користувача після успішного створення замовлення
            cartService.clearCart(user);

            // Додаємо повідомлення про успіх та ID замовлення для відображення на сторінці підтвердження
            redirectAttributes.addFlashAttribute("orderId", newOrder.getId()); // Передаємо ID замовлення
            redirectAttributes.addFlashAttribute("successMessage", "Ваше замовлення успішно оформлено!");


            // Перенаправляємо на сторінку підтвердження замовлення
            // ЗМІНЮЄМО /order/success НА /cart/order/success
            return new RedirectView("/cart/order/success", true); // true - вказує на зовнішній редірек

        } catch (Exception e) {
            // Обробка будь-яких винятків, що виникли під час оформлення замовлення.
            e.printStackTrace();
            System.err.println("Error placing order: " + e.getMessage());


            // Передаємо повідомлення про помилку на сторінку кошика
            redirectAttributes.addFlashAttribute("errorMessage", "Сталася помилка під час оформлення замовлення. Будь ласка, спробуйте ще раз.");
            return new RedirectView("/cart", true);
        }
    }

    // Метод для відображення сторінки успіху замовлення
    @GetMapping("/order/success") // Обробляє GET /order/success
    public String orderSuccessPage(Model model) {
        // Дані (successMessage, orderId) можуть бути доступні тут через FlashAttributes,
        // якщо ви перенаправляли з RedirectAttributes.addFlashAttribute()

        // Додаємо дані для хедера/футера (адаптуйте за потреби)
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("navReviews", "Відгуки");

        return "order-success"; // Повертаємо назву шаблону order-success.html
    }


    // ДОПОМІЖНИЙ МЕТОД

    // Допоміжний метод для отримання об'єкта User з Authentication
    // Потрібен UserService для пошуку користувача в базі за username (email)
    private User getUserByAuthentication(Authentication authentication) {
        // Перевіряємо, чи об'єкт authentication не null і користувач аутентифікований.
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); // Отримуємо username (email) користувача
            // Використовуємо ваш UserService для пошуку користувача за email
            return userService.findByEmail(username); // Потрібен метод findByEmail(String email) у вашому UserService
        }
        // Якщо користувач не аутентифікований, повертаємо null.
        return null; // Користувач не аутентифікований або не знайдений
    }
}