package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface ManufacturedItemRepository extends JpaRepository<ManufacturedItem, Long> {
    Page<ManufacturedItem> findByPackageItem_RootPackagedMedicinalProduct_MedicinalProduct_Country(String country, Pageable pageable);

    Optional<ManufacturedItem> findByIdAndPackageItem_RootPackagedMedicinalProduct_MedicinalProduct_Country(Long id, String country);
}
