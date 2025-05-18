package OM.Bakery.service;

import OM.Bakery.model.User;
import OM.Bakery.repository.UserRepository; // Імпорт вашого UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Імпорт PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// Сервіс для виконання операцій, пов'язаних з користувачами.
@Service
public class UserService {

    // Репозиторій для доступу до даних користувачів у базі даних.
    private final UserRepository userRepository;
    // Компонент для кодування паролів.
    private final PasswordEncoder passwordEncoder;

    // Єдиний конструктор для інжекції залежностей
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Метод для реєстрації нового користувача в системі.
    @Transactional
    public void registerNewUserAccount(User user) throws IllegalArgumentException {
        // Перевіряємо, чи користувач з таким email вже існує в базі даних.
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            // Якщо існує, викидаємо виняток.
            throw new IllegalArgumentException("Користувач з такою електронною поштою вже існує.");
        }

        // Кодуємо пароль користувача перед його збереженням у базі даних.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Зберігаємо нового користувача в базі даних через репозиторій.
        userRepository.save(user);
    }

    // Метод для перевірки, чи є email унікальним (чи ще не зайнятий).
    public boolean isEmailUnique(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


    // Метод для пошуку користувача за його електронною поштою (яка використовується як username).
    public User findByEmail(String email) {
        // Викликаємо метод репозиторію для пошуку користувача за email.
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // Повертаємо User або null, якщо не знайдено
    }

}