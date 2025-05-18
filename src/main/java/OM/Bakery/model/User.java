package OM.Bakery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email; // Додайте імпорт
import jakarta.validation.constraints.NotBlank; // Додайте імпорт
import jakarta.validation.constraints.Size; // Додайте імпорт


@Entity
@Table(name = "users") // Мапує сутність на таблицю з назвою "users".
public class User { // Клас, що представляє користувача системи.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ім'я не може бути порожнім") // Приклад валідації
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName; // Теж потрібна валідація, поле обов'язкове

    @NotBlank(message = "Email не може бути порожнім") // Приклад валідації
    @Email(message = "Введіть коректний формат email") // Приклад валідації
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Пароль не може бути порожнім") // Приклад валідації
    @Size(min = 6, message = "Пароль має бути не менше 6 символів") // Приклад валідації
    @Column(name = "password", nullable = false)
    private String password;

    // Конструктори (можна додати порожній конструктор за замовчуванням)
    public User() {
    }

    // Конструктор для створення нового об'єкта користувача з основними полями.
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Геттери та сеттери

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}