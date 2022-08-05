package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
public class Substance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "activeIngredient")
    private Set<PharmaceuticalProduct> usedAsActiveIngredientMedicinalProduct;

    @OneToMany(mappedBy = "modifier")
    private Set<PharmaceuticalProduct> usedAsModifierMedicinalProduct;
}

