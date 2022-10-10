package it.datawizard.unicom.unicombackend.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@JsonIdentityInfo(property = "atcCode", generator = ObjectIdGenerators.PropertyGenerator.class)
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
