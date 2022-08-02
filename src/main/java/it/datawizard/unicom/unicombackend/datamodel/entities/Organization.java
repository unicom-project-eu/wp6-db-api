package it.datawizard.unicom.unicombackend.datamodel.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "marketingAuthorizationOlder")
    private Set<MedicinalProductDefinition> medicinalProducts;
}
