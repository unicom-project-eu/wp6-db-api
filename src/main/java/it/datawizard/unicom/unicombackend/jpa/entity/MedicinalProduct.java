package it.datawizard.unicom.unicombackend.jpa.entity;

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
public class MedicinalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String mpId;

    @Column(nullable = false)
    private String fullName;

    private String packDescription;

    @Column(nullable = false)
    private String country; // TODO make an entity

    @OneToOne
    @JoinColumn()
    private Strength authorizedStrength;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PharmaceuticalProduct pharmaceuticalProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Organization marketingAuthorizationHolder;

    @OneToMany(mappedBy = "medicinalProduct")
    @ToString.Exclude
    private Set<PackagedMedicinalProduct> packagedMedicinalProducts;

    @ManyToMany
    @JoinTable()
    @ToString.Exclude
    private Set<EdqmRouteOfAdministration> routesOfAdministration;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EdqmUnitOfPresentation unitOfPresentation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EdqmDoseForm pharmaceuticalDoseForm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MedicinalProduct that = (MedicinalProduct) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
