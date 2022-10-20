package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagedMedicinalProductRepository extends JpaRepository<PackagedMedicinalProduct, Long> {
    PackagedMedicinalProduct findByPcId(String value);
}
