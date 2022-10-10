package it.datawizard.unicom.unicombackend.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AtcCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String atcCode;

    @ManyToOne
    @JoinColumn()
    private MedicinalProduct medicinalProduct;

    public AtcCode(String atcCode) {
        this.atcCode = atcCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AtcCode atcCode = (AtcCode) o;
        return id != null && Objects.equals(id, atcCode.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
