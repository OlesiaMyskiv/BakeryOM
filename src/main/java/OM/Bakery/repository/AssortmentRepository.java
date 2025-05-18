package OM.Bakery.repository;

import OM.Bakery.model.Assortment;
import org.springframework.data.jpa.repository.JpaRepository;

// Інтерфейс репозиторію для сутності Assortment.
public interface AssortmentRepository extends JpaRepository<Assortment, Long> {
}