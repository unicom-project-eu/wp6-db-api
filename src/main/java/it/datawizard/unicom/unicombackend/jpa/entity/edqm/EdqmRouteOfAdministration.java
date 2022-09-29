package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmRouteOfAdministration extends EdqmConcept {
    @ManyToMany
    @ToString.Exclude
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;
}

