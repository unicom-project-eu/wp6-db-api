package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MedicinalProductRepository extends JpaRepository<MedicinalProduct, Long> {
    MedicinalProduct findByFullName(String name);

    MedicinalProduct findByMpId(String mpId);

    List<MedicinalProduct> findByAtcCodesIn(Set<String> atcCodes);
}
