package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.enums.IEdqmEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmUnitOfPresentation extends EdqmConcept {
    @OneToMany(mappedBy = "unitOfPresentation")
    private Set<ManufacturedItem> manufacturedItems;

    @OneToMany(mappedBy = "unitOfPresentation")
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;
}