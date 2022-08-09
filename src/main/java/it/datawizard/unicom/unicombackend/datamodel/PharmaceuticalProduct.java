package it.datawizard.unicom.unicombackend.datamodel;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @ToString.Exclude
    private Set<MedicinalProduct> medicinalProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PharmaceuticalProduct that = (PharmaceuticalProduct) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
