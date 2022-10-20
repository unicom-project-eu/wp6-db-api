package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmaceuticalProductRepository extends JpaRepository<PharmaceuticalProduct, Long> {

    @Override
    @EntityGraph(attributePaths = {"administrableDoseForm","unitOfPresentation","routesOfAdministration"})
    Page<PharmaceuticalProduct> findAll(Pageable pageable);
}
