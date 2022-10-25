package it.datawizard.unicom.unicombackend.jpa.specification;

import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import org.springframework.data.jpa.domain.Specification;


public final class IngredientSpecifications {
    public static Specification<Ingredient> isRoleEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.get("role"), expression);
    }

    public static Specification<Ingredient> isSubstanceCodeEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("substance").get("substanceCode"), expression);
    }

    public static Specification<Ingredient> isCountryEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("manufacturedItem").join("packageItem").join("rootPackagedMedicinalProduct").join("medicinalProduct").get("country"), expression);
    }

    public static Specification<Ingredient> isManufacturedItemEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("manufacturedItem").get("id"), expression);
    }
}
