package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.datawizard.unicom.unicombackend.jackson.idresolver.EdqmUnitOfPresentationIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EdqmUnitOfPresentationIdResolver.class)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmUnitOfPresentation extends EdqmConcept {
    @OneToMany(mappedBy = "unitOfPresentation")
    @ToString.Exclude
    private Set<ManufacturedItem> manufacturedItems;

    @OneToMany(mappedBy = "unitOfPresentation")
    @ToString.Exclude
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;
}