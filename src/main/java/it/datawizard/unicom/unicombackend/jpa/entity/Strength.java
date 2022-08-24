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

    private String display;

    // TODO normalize units
    // Reference Strength
    @Column(nullable = false)
    private Float referenceNumeratorValue;

    @Column(nullable = false)
    private String referenceNumeratorUnit;

    private Float referenceDenominatorValue;
    private String referenceDenominatorUnit;

    // Strength
    private Float numeratorValue;
    private String numeratorUnit;
    private Float denominatorValue;
    private String denominatorUnit;

    @Column(nullable = false)
    private Boolean isPresentationStrength;

    @OneToOne(mappedBy = "normalizedStrength")
    private PharmaceuticalProduct pharmaceuticalProduct;

    @OneToOne(mappedBy = "authorizedStrength")
    private MedicinalProduct medicinalProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EdqmUnitOfPresentation unitOfPresentation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SubstanceWithRolePai referenceSubstance;

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
