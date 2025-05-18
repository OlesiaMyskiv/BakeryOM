package OM.Bakery.service;

import OM.Bakery.model.User;
import OM.Bakery.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

// Реалізація інтерфейсу UserDetailsService Spring Security для завантаження даних користувача під час аутентифікації.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Репозиторій для пошуку користувачів у базі даних.
    private final UserRepository userRepository;

    // Конструктор класу з ін'єкцією залежності UserRepository.
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Метод, який викликається Spring Security для завантаження деталей користувача за його "логіном" (тут - email).
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Шукаємо користувача в базі даних за наданим email.
        // Якщо користувача не знайдено, викидаємо виняток UsernameNotFoundException.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з такою електронною поштою не знайдено: " + email));

        // Створюємо та повертаємо об'єкт UserDetails (реалізація Spring Security) на основі даних знайденого користувача.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")) // Додаємо повноваження (роль) "USER".
        );
    }
}