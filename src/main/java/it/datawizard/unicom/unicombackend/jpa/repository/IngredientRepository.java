package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
     Ingredient findBySubstance_SubstanceCode(String code);

     Page<Ingredient> findByManufacturedItem_PackageItem_RootPackagedMedicinalProduct_MedicinalProduct(
             MedicinalProduct medicinalProduct, Pageable pageable);
}
