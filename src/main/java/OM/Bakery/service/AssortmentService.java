package OM.Bakery.service;

import OM.Bakery.model.Assortment;
import OM.Bakery.repository.AssortmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Сервіс для виконання операцій, пов'язаних з асортиментом товарів.
@Service
public class AssortmentService {

    // Репозиторій для доступу до даних асортименту в базі даних.
    private final AssortmentRepository assortmentRepository;

    // Конструктор класу з ін'єкцією залежності AssortmentRepository.
    public AssortmentService(AssortmentRepository assortmentRepository) {
        this.assortmentRepository = assortmentRepository;
    }

    // Метод для отримання списку всіх товарів асортименту.
    public List<Assortment> getAllAssortments() {
        return assortmentRepository.findAll(); // Викликаємо стандартний метод репозиторію для отримання всіх записів.
    }

    // Метод для пошуку товару асортименту за його унікальним ідентифікатором (ID).
    public Optional<Assortment> findById(Long id) {
        return assortmentRepository.findById(id); // Викликаємо стандартний метод репозиторію для пошуку за ID.
    }
}