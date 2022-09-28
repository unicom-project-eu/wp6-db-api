package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmaceuticalProductRepository extends JpaRepository<PharmaceuticalProduct, Long> {
}
