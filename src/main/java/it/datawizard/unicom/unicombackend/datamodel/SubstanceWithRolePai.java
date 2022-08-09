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
public class SubstanceWithRolePai {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "activeIngredient")
    @ToString.Exclude
    private Set<PharmaceuticalProduct> usedAsActiveIngredientMedicinalProduct;

    @OneToMany(mappedBy = "modifier")
    @ToString.Exclude
    private Set<PharmaceuticalProduct> usedAsModifierMedicinalProduct;

    @OneToMany(mappedBy = "referenceSubstance")
    @ToString.Exclude
    private Set<Strength> strengths;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SubstanceWithRolePai that = (SubstanceWithRolePai) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

