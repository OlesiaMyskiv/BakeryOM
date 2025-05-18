package OM.Bakery.config;

import OM.Bakery.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig { // Клас для налаштування безпеки застосунку.

    // Сервіс, що завантажує дані користувача.
    private final UserDetailsServiceImpl userDetailsService;

    // Конструктор класу з ін'єкцією UserDetailsService.
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Бін для кодування паролів.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Використовуємо BCrypt для хешування.
    }

    // Бін для провайдера аутентифікації.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Встановлюємо сервіс користувачів.
        authProvider.setPasswordEncoder(passwordEncoder()); // Встановлюємо енкодер паролів.
        return authProvider; // Повертаємо налаштований провайдер.
    }

    // Конфігурація ланцюжка фільтрів безпеки.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF захист увімкнено за замовчуванням.
                // .csrf(csrf -> csrf.disable()) // Цей рядок ВИДАЛЕНО або ЗАКОМЕНТОВАНО (захист активний).
                .authenticationProvider(authenticationProvider()) // Реєстрація кастомного провайдера.
                // Налаштування правил авторизації для запитів.
                .authorizeHttpRequests((requests) -> requests
                        // Дозволяємо доступ без аутентифікації до публічних сторінок та статики.
                        .requestMatchers("/", "/registration", "/registration-success", "/login-form",
                                "/about", "/assortment", "/cart", "/cart/add", "/reviews",
                                "/cart/order/success", // Сторінка успіху замовлення.
                                "/css/**", "/img/**", "/js/**").permitAll() // Доступ дозволено всім.
                        // Всі інші запити вимагають аутентифікації.
                        .anyRequest().authenticated() // Доступ тільки для аутентифікованих.
                )
                // Налаштування форми входу.
                .formLogin((form) -> form
                        .loginPage("/login-form") // URL сторінки входу.
                        .loginProcessingUrl("/perform_login") // URL для обробки даних входу (POST).
                        .defaultSuccessUrl("/", true) // Куди перенаправити після успішного входу.
                        .failureUrl("/login-form?error") // Куди перенаправити у випадку помилки входу.
                        .permitAll() // Сторінка входу доступна всім.
                )
                // Налаштування виходу з системи.
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL для логауту (POST).
                        .logoutSuccessUrl("/") // Куди перенаправити після логауту.
                        .invalidateHttpSession(true) // Анулювати сесію.
                        .clearAuthentication(true) // Очистити аутентифікацію.
                        .permitAll() // Доступ до логауту дозволено всім.
                )
                // Обробка винятків (наприклад, коли потрібна аутентифікація).
                .exceptionHandling((exceptionHandling) ->
                        // Перенаправляємо на сторінку входу, якщо потрібна аутентифікація.
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login-form"); // Перенаправлення на сторінку входу.
                        })
                );

        return http.build(); // Побудова та повернення ланцюжка фільтрів.
    }
}