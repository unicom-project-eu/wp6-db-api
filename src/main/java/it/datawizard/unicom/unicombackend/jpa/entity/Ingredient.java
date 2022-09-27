package it.datawizard.unicom.unicombackend.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String role;

    @OneToOne(optional = false)
    private Strength referenceStrength;

    @OneToOne
    private Strength strength;

    @ManyToOne
    @JoinColumn()
    private Substance substance;

    @ManyToMany
    @Column(nullable = false)
    @JoinTable()
    @ToString.Exclude
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;

    @ManyToMany
    @Column(nullable = false)
    @JoinTable()
    @ToString.Exclude
    private Set<ManufacturedItem> manufacturedItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ingredient ingredient = (Ingredient) o;
        return id != null && Objects.equals(id, ingredient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}