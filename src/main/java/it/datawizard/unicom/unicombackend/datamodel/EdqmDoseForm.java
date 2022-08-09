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
public class EdqmDoseForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    @OneToMany
    @ToString.Exclude
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EdqmDoseForm that = (EdqmDoseForm) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
