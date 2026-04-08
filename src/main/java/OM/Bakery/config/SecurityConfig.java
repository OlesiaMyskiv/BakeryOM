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
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests((requests) -> requests
                        // 1. Спочатку дозволяємо доступ до публічних сторінок та статики
                        .requestMatchers("/", "/registration", "/registration-success", "/login-form",
                                "/about", "/assortment", "/cart", "/cart/add", "/reviews",
                                "/cart/order/success", "/css/**", "/img/**", "/js/**").permitAll()

                        // 2. Потім вказуємо правила для конкретних сторінок (якщо потрібно)
                        .requestMatchers("/profile").authenticated()

                        // 3. І ТІЛЬКИ В КІНЦІ — правило для всіх інших запитів
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login-form")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login-form?error")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login-form");
                        })
                );

        return http.build();
    }
}