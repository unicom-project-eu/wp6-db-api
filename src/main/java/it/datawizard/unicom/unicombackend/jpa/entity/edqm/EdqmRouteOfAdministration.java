package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.datawizard.unicom.unicombackend.jackson.idresolver.EdqmRouteOfAdministrationIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EdqmRouteOfAdministrationIdResolver.class)
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

