package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicinalProductRepository extends JpaRepository<MedicinalProduct, Long> {
}
