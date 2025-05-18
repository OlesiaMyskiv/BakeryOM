package OM.Bakery.repository;

import OM.Bakery.model.CartItem;
import OM.Bakery.model.User; // Імпорт User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Інтерфейс репозиторію для сутності CartItem
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Знайти всі елементи кошика для конкретного користувача
    List<CartItem> findByUser(User user);

    // Знайти елемент кошика конкретного користувача для певного товару за ID товару
    Optional<CartItem> findByUserAndAssortment_Id(User user, Long assortmentId);

    // Видалити всі елементи кошика для конкретного користувача
    void deleteByUser(User user);
}