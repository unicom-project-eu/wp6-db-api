package it.datawizard.unicom.unicombackend.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmRouteOfAdministration;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmUnitOfPresentation;
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

    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
    private EdqmDoseForm administrableDoseForm;

    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
    private EdqmUnitOfPresentation unitOfPresentation;

    @ManyToMany(mappedBy = "pharmaceuticalProducts")
    @ToString.Exclude
    private Set<Ingredient> ingredients;

    @ManyToMany
    @JoinTable()
    @ToString.Exclude
    private Set<EdqmRouteOfAdministration> routesOfAdministration;

    @OneToMany (mappedBy = "pharmaceuticalProduct")
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
