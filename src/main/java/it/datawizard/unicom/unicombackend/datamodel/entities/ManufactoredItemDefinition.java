package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.DoseForm;
import it.datawizard.unicom.unicombackend.datamodel.RouteOfAdministration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
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
    private Set<ProductContainsItem> containedBy;

    @OneToMany(mappedBy = "manufactoredItemDefinition")
    private Set<ItemIngredientSubstance> ingredients;
}
