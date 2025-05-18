package OM.Bakery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController { // Контролер для обробки запитів, пов'язаних зі сторінкою "Про нас".

    @GetMapping("/about") // Мапінг GET запитів на URL "/about" до цього методу.
    public String showAboutPage(Model model) { // Метод для відображення сторінки "Про нас". Приймає Model для передачі даних у Thymeleaf
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");

        return "about";// Повертає ім'я шаблону представлення (about.html)
    }
}