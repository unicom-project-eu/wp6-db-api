package it.datawizard.unicom.unicombackend.jpa.specification;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.jpa.domain.Specification;

public final class MedicinalProductSpecifications {
    public static Specification<MedicinalProduct> isFullNameEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.get("fullName"), expression);
    }

    public static Specification<MedicinalProduct> atcCodesContains(String expression) {
        return (root, query, builder) -> builder.equal(root.join("atcCodes").get("atcCode"), expression);
    }

    public static Specification<MedicinalProduct> isCountryEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.get("country"), expression);
    }
}
