package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MedicinalProductRepository extends JpaRepository<MedicinalProduct, Long>, JpaSpecificationExecutor<MedicinalProduct> {
    Optional<MedicinalProduct> findByIdAndCountry(Long id, String country);
    Page<MedicinalProduct> findByCountry(String country, Pageable pageable);
}
