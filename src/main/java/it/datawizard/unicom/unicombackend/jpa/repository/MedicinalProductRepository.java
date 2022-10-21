package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MedicinalProductRepository extends JpaRepository<MedicinalProduct, Long> {
    MedicinalProduct findByFullName(String name);

    MedicinalProduct findByMpId(String mpId);

    List<MedicinalProduct> findByAtcCodesIn(Set<String> atcCodes);

    Optional<MedicinalProduct> findByIdAndCountry(Long id, String country);
    Page<MedicinalProduct> findByCountry(String country, Pageable pageable);
}
