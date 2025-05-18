package OM.Bakery.controller;

import OM.Bakery.model.User;
import OM.Bakery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController { // Контролер для обробки запитів аутентифікації та реєстрації.

    // Сервіс для виконання операцій з користувачами.
    private final UserService userService;

    // Конструктор класу з ін'єкцією залежності UserService.
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Обробляє GET запит для відображення форми реєстрації
    // Мапиться на GET /registration
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");

        return "registration"; // Повертає назву шаблону "registration.html"
    }

    // Обробляє POST запит для обробки даних реєстрації з форми
    // Мапиться на POST /registration
    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid User user, // Отримує об'єкт користувача з форми та валідує його.
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

        // Перевіряємо, чи є помилки валідації.
        if (result.hasErrors()) {
            model.addAttribute("navAbout", "Про нас");
            model.addAttribute("navAssortment", "Асортимент");
            model.addAttribute("headerTitle", "Cake House");
            model.addAttribute("navReviews", "Відгуки");
            return "registration"; // Повертаємося на шаблон "registration" у випадку помилок
        }

        try {
            // Викликаємо сервіс для реєстрації нового користувача.
            userService.registerNewUserAccount(user);

            // Додаємо "flash" атрибут для відображення повідомлення про успіх після редиректу.
            redirectAttributes.addFlashAttribute("registrationSuccess", true);

            // Перенаправлення на сторінку успішної реєстрації.
            return "redirect:/registration-success";

        } catch (IllegalArgumentException e) { // Обробка помилки, якщо користувач з такою поштою вже існує.
            model.addAttribute("emailError", e.getMessage());
            model.addAttribute("navAbout", "Про нас");
            model.addAttribute("navAssortment", "Асортимент");
            model.addAttribute("headerTitle", "Cake House");
            model.addAttribute("navReviews", "Відгуки");

            return "registration"; // Повертаємося на шаблон "registration"
        } catch (Exception e) { // Обробка інших неочікуваних помилок
            model.addAttribute("registrationError", "Виникла неочікувана помилка під час реєстрації.");
            model.addAttribute("navAbout", "Про нас");
            model.addAttribute("navAssortment", "Асортимент");
            model.addAttribute("headerTitle", "Cake House");
            model.addAttribute("navReviews", "Відгуки");
            return "registration"; // Повертаємося на шаблон "registration"
        }
    }

    // Мапінг для сторінки успішної реєстрації
    @GetMapping("/registration-success")
    public String showRegistrationSuccessPage(Model model) {
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");
        // ... інші атрибути для хедера, якщо потрібні ...
        return "registration-success"; // Повертає назву шаблону "registration-success", що відповідає файлу registration-success.html
    }

    // Мапінг для сторінки входу
    @GetMapping("/login-form")
    public String showLoginForm(Model model) {
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");

        return "login-form"; // Повертає назву шаблону "login-form"
    }
}