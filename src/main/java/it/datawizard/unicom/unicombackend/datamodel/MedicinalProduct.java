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
public class MedicinalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mpId;
    private String fullName;
    private Integer packSize;
    private String packDescription;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PharmaceuticalProduct pharmaceuticalProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EdqmUnitOfPresentation edqmUnitOfPresentation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Organization marketingAuthorizationHolder;

    @OneToMany(mappedBy = "medicinalProduct")
    @ToString.Exclude
    private Set<PackagedMedicinalProduct> packagedMedicinalProducts;

    @ManyToMany
    @ToString.Exclude
    Set<EdqmRouteOfAdministration> routesOfAdministration;

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
