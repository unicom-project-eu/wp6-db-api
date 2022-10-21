package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackagedMedicinalProductRepository extends JpaRepository<PackagedMedicinalProduct, Long> {
    PackagedMedicinalProduct findByPcId(String value);

    Optional<PackagedMedicinalProduct> findByIdAndMedicinalProduct_Country(Long id, String country);

    Page<PackagedMedicinalProduct> findByMedicinalProduct_Country(String country, Pageable pageable);
}
