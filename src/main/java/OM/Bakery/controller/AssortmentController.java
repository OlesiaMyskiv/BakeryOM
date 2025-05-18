package OM.Bakery.controller;

import OM.Bakery.service.AssortmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AssortmentController { // Контролер для обробки запитів, пов'язаних зі сторінкою асортименту.

    @Autowired
    private AssortmentService service; // Поле для доступу до сервісу асортименту.

    @GetMapping("/assortment") // Мапінг GET запитів на URL "/assortment" до цього методу.
    public String showAssortmentPage(Model model) { // Метод для відображення сторінки асортименту. Приймає Model.
        model.addAttribute("products", service.getAllAssortments());

        model.addAttribute("title", "Асортимент");
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("navReviews", "Відгуки");
        model.addAttribute("headerTitle", "Cake House");

        return "assortment"; // Повертає ім'я шаблону представлення (assortment.html)
    }
}