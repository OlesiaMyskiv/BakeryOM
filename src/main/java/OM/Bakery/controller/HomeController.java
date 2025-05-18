package OM.Bakery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { // Контролер для обробки запитів, пов'язаних з головною сторінкою.

        @GetMapping("/") // Мапінг GET запитів на кореневий URL "/" до цього методу.
        public String index(Model model) {
            model.addAttribute("heroSubtitle", "КОНДИТЕРСЬКА");
            model.addAttribute("heroTitle", "BAKERY");
            model.addAttribute("headerTitle", "Cake House");
            model.addAttribute("navAbout", "Про нас");
            model.addAttribute("navAssortment", "Асортимент");
            model.addAttribute("navReviews", "Відгуки");
            return "index";  // Повертає ім'я шаблону представлення (index.html).
        }

}

