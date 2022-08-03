package it.datawizard.unicom.unicombackend.datamodel.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Ingredient {
    @Embeddable
    @Getter
    @Setter
    public static class Key implements Serializable {
        private long manufactoredItemDefinitionId;
        private long substanceDefinitionId;
    }

    @EmbeddedId
    private Ingredient.Key id;

    @ManyToOne
    @MapsId("manufactoredItemDefinitionId")
    private ManufactoredItemDefinition manufactoredItemDefinition;

    @ManyToOne
    @MapsId("substanceDefinitionId")
    private SubstanceDefinition substanceDefinition;

    private float strengthPresentation;
    private float strengthConcentration;
    private String referenceSubstance;
    private float referenceStrength;

}
