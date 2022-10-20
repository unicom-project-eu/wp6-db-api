package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
     Ingredient findBySubstance_SubstanceCode(String code);

     @Override
     @EntityGraph(attributePaths = {"substance","manufacturedItem.manufacturedDoseForm","manufacturedItem.unitOfPresentation","manufacturedItem.packageItem.packagedMedicinalProduct.medicinalProduct.atcCodes","manufacturedItem.packageItem.packagedMedicinalProduct.medicinalProduct.pharmaceuticalProduct","manufacturedItem.packageItem.packagedMedicinalProduct.medicinalProduct.pharmaceuticalProduct.routesOfAdministration"})
     Page<Ingredient> findAll(Pageable pageable);
}
