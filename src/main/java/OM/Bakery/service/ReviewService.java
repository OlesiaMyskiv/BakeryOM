package OM.Bakery.service;

import OM.Bakery.model.Review;
import OM.Bakery.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Сервіс для виконання операцій, пов'язаних з відгуками.
@Service
public class ReviewService {

    // Репозиторій для доступу до даних відгуків у базі даних.
    private final ReviewRepository reviewRepository;

    // Конструктор класу з ін'єкцією залежності ReviewRepository.
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Метод для отримання списку всіх відгуків.
    public List<Review> getAllReviews() {
        return reviewRepository.findAll(); // Викликаємо метод репозиторію для отримання всіх записів.
    }

    // Метод для додавання нового відгуку.
    public void addReview(Review review) {
        reviewRepository.save(review); // Викликаємо метод репозиторію для збереження сутності.
    }
}