package OM.Bakery.controller;

import OM.Bakery.model.Review;
import OM.Bakery.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ReviewsController { // Контролер для обробки запитів, пов'язаних зі сторінкою відгуків.

    // Сервіс для виконання операцій з відгуками.
    private final ReviewService reviewService;

    // Конструктор класу з ін'єкцією залежності ReviewService.
    public ReviewsController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Обробляє GET запит на URL "/reviews".
    @GetMapping("/reviews")
    public String showReviewsPage(Model model) {
        model.addAttribute("navAbout", "Про нас");
        model.addAttribute("navAssortment", "Асортимент");
        model.addAttribute("headerTitle", "Cake House");
        model.addAttribute("navReviews", "Відгуки");

        List<Review> reviews = reviewService.getAllReviews();  // Отримуємо список усіх відгуків через сервіс.
        model.addAttribute("reviews", reviews);  // Додаємо список відгуків у модель.
        model.addAttribute("newReview", new Review()); // Для форми додавання нового відгуку

        return "reviews";
    }

    // Метод для додавання нового відгуку з форми. Приймає об'єкт Review з даних форми.
    @PostMapping("/reviews/add")
    public String addReview(@ModelAttribute("newReview") Review review) {
        reviewService.addReview(review); // Зберігаємо новий відгук через сервіс.
        return "redirect:/reviews"; // Перенаправлення на сторінку відгуків після додавання
    }
}