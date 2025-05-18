package OM.Bakery.repository;

import OM.Bakery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Інтерфейс репозиторію для сутності Order.
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring Data JPA автоматично надасть базові методи CRUD (збереження, пошук за ID, видалення тощо).
}