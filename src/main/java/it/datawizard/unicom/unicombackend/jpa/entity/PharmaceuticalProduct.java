package it.datawizard.unicom.unicombackend.jpa.entity;

import it.datawizard.unicom.unicombackend.jpa.enums.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.enums.EdqmUnitOfPresentation;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String idmpPhpId;

    @Enumerated(EnumType.STRING)
    private EdqmDoseForm administrableDoseForm;

    @Enumerated(EnumType.STRING)
    private EdqmUnitOfPresentation unitOfPresentation;

    @ManyToMany(mappedBy = "pharmaceuticalProducts")
    @ToString.Exclude
    private Set<Ingredient> ingredients;

    @OneToMany (mappedBy = "pharmaceuticalProduct")
    private Set<RouteOfAdministration> routesOfAdministration;

    @OneToMany (mappedBy = "pharmaceuticalProduct")
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
