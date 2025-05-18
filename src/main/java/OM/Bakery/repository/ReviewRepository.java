package OM.Bakery.repository;

import OM.Bakery.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

// Інтерфейс репозиторію для сутності Review.
public interface ReviewRepository extends JpaRepository<Review, Long> {
}