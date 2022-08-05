package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
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
    private Substance activeIngredient;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Substance modifier;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DoseForm doseForm;

    @OneToMany(mappedBy = "pharmaceuticalProduct")
    private Set<MedicinalProduct> medicinalProducts;
}
