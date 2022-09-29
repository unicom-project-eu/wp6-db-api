package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmDoseForm extends EdqmConcept {
    @OneToMany(mappedBy = "manufacturedDoseForm")
    private Set<ManufacturedItem> manufacturedItems;

    @OneToMany(mappedBy = "authorizedPharmaceuticalDoseForm")
    private Set<MedicinalProduct> medicinalProducts;

    @OneToMany(mappedBy = "administrableDoseForm")
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;
}
