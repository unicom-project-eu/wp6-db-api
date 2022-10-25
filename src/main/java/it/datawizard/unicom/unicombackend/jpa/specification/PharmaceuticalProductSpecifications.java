package it.datawizard.unicom.unicombackend.jpa.specification;

import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import org.springframework.data.jpa.domain.Specification;


public final class PharmaceuticalProductSpecifications {
    public static Specification<PharmaceuticalProduct> isAdministrableDoseFormEqualTo(String expression) {
        return (root, query, builder) -> builder.equal(root.join("administrableDoseForm").get("code"), expression);
    }

    public static Specification<PharmaceuticalProduct> routesOfAdministrationContains(String expression) {
        return (root, query, builder) -> builder.equal(root.join("routesOfAdministration").get("code"), expression);
    }
}
