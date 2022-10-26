package it.datawizard.unicom.unicombackend.jpa.specification;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import org.springframework.data.jpa.domain.Specification;


public final class ManufacturedItemSpecifications {
    public static Specification<ManufacturedItem> isManufacturedDoseFormEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("manufacturedDoseForm").get("code"), expression);
    }

    public static Specification<ManufacturedItem> ingredientsContain(String expression) {
        return (root, query, builder) -> builder.equal(root.join("ingredients").join("substance").get("substanceCode"), expression);
    }

    public static Specification<ManufacturedItem> isCountryEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("packageItem").join("rootPackagedMedicinalProduct").join("medicinalProduct").get("country"), expression);
    }
}
