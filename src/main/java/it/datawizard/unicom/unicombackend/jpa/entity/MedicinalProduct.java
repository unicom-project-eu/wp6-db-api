package it.datawizard.unicom.unicombackend.jpa.entity;

import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Document(indexName = "medicinal_product")
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

    @Field(type = FieldType.Text)
    private String fullName;

    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
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
