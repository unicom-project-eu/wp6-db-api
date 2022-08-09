package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class SubstanceWithRolePAI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "activeIngredient")
    private Set<PharmaceuticalProduct> usedAsActiveIngredientMedicinalProduct;

    @OneToMany(mappedBy = "modifier")
    private Set<PharmaceuticalProduct> usedAsModifierMedicinalProduct;
}

