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
public class EdqmDoseForm {
    @Id
    private String code;

    @Column(nullable = false)
    private String display;

    @OneToMany(mappedBy = "pharmaceuticalDoseForm")
    @ToString.Exclude
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;

    @OneToMany(mappedBy = "administrableDoseForm")
    @ToString.Exclude
    private Set<MedicinalProduct> medicinalProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EdqmDoseForm that = (EdqmDoseForm) o;
        return code != null && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
