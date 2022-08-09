package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class PharmaceuticalProduct {
    public enum ReferenceSubstance {
        unitOfPresentationBased,
        concentrationBased,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phpId;
    private String ingredientCode;

    private Float numeratorValue;
    private Float denominatorValue;

    // TODO normalize unit by decomposing it into unit and multiplier
    //      (eg: for "ml" separate "milli" from "liters")
    private String numeratorUnit;
    private String denominatorUnit;

    @Enumerated(EnumType.STRING)
    private ReferenceSubstance referenceSubstance;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SubstanceWithRolePai activeIngredient;

    @ManyToOne
    @JoinColumn(nullable = true)
    private SubstanceWithRolePai modifier;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EdqmDoseForm edqmDoseForm;

    @OneToMany(mappedBy = "pharmaceuticalProduct")
    private Set<MedicinalProduct> medicinalProducts;
}
