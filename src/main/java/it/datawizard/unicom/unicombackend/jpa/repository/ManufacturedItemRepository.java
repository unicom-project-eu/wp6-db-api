package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface ManufacturedItemRepository extends JpaRepository<ManufacturedItem, Long>, JpaSpecificationExecutor<ManufacturedItem> {
    Optional<ManufacturedItem> findByIdAndPackageItem_RootPackagedMedicinalProduct_MedicinalProduct_Country(Long id, String country);
}
