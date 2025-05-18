package OM.Bakery.repository;

import OM.Bakery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Інтерфейс репозиторію для сутності User.
public interface UserRepository extends JpaRepository<User, Long> {

    // Знаходить користувача в базі даних за його електронною поштою.
    Optional<User> findByEmail(String email);
}