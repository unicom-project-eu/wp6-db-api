package it.datawizard.unicom.unicombackend.jpa.specification;

import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import org.springframework.data.jpa.domain.Specification;


public final class SubstanceSpecifications {
    public static Specification<Substance> isSubstanceCodeEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.get("substanceCode"), expression);
    }

    public static Specification<Substance> isSubstanceNameEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.get("substanceName"), expression);
    }
}
