package it.datawizard.unicom.unicombackend.datamodel.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class SubstanceDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "substanceDefinition")
    private Set<Ingredient> usedBy;
}
