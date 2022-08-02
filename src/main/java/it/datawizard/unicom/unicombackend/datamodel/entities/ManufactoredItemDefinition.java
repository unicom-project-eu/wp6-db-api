package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.DoseForm;
import it.datawizard.unicom.unicombackend.datamodel.RouteOfAdministration;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ManufactoredItemDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String phpId;

    private DoseForm doseForm;

    private String atcCode;

    private RouteOfAdministration routeOfAdministration;

    @OneToMany(mappedBy = "manufactoredItemDefinition")
    Set<ProductContainsItem> containedBy;
}
