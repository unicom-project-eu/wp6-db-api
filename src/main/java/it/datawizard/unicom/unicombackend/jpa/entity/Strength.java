package it.datawizard.unicom.unicombackend.jpa.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Strength {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Reference Strength
    private Float concentrationNumeratorValue;
    private String concentrationNumeratorUnit;

    private Float concentrationDenominatorValue;
    private String concentrationDenominatorUnit;

    // Strength
    private Float presentationNumeratorValue;
    private String presentationNumeratorUnit;

    private Float presentationDenominatorValue;
    private String presentationDenominatorUnit;

    @OneToOne(mappedBy = "referenceStrength")
    private Ingredient ingredientWithReferenceStrength;

    @OneToOne(mappedBy = "strength")
    private Ingredient ingredientWithStrength;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Strength strength = (Strength) o;
        return id != null && Objects.equals(id, strength.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
