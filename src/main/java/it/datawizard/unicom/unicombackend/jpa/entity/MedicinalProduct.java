package it.datawizard.unicom.unicombackend.jpa.entity;

import it.datawizard.unicom.unicombackend.jpa.enums.EdqmDoseForm;
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


    private String country;


    private String fullName;


    private EdqmDoseForm authorizedPharmaceuticalDoseForm;


    //TODO Decide for Organization entity usage
    private String marketingAuthorizationHolder;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PharmaceuticalProduct pharmaceuticalProduct;

    @OneToMany(mappedBy = "medicinalProduct")
    @ToString.Exclude
    private Set<PackagedMedicinalProduct> packagedMedicinalProducts;

    @OneToMany (mappedBy = "medicinalProduct")
    @ToString.Exclude
    private Set<AtcCode> atcCodes;



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
