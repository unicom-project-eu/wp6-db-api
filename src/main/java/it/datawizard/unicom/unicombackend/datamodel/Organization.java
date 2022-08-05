package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "marketingAuthorizationHolder")
    private Set<MedicinalProduct> medicinalProducts;
}

