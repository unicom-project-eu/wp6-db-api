package it.datawizard.unicom.unicombackend.datamodel;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PackagedMedicinalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pcId;
    private String atcCode;
    private Integer packageSize;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MedicinalProduct medicinalProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PackagedMedicinalProduct that = (PackagedMedicinalProduct) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

