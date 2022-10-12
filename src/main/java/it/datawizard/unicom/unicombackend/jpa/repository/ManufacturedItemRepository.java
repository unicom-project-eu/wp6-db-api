package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturedItemRepository extends JpaRepository<ManufacturedItem, Long> {
}
