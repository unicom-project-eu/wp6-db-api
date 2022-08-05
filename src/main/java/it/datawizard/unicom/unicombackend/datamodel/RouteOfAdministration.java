package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
public class RouteOfAdministration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    @ManyToMany
    private Set<MedicinalProduct> medicinalProducts;
}

