package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.UnitOfPresentation;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProductContainsItem {
    @Embeddable
    public static class Key implements Serializable {
        private long medicinalProductDefinitionId;
        private long manufactoredItemDefinitionId;
    }

    @EmbeddedId
    ProductContainsItem.Key id;

    @ManyToOne
    @MapsId("medicinalProductDefinitionId")
    MedicinalProductDefinition medicinalProductDefinition;

    @ManyToOne
    @MapsId("manufactoredItemDefinitionId")
    ManufactoredItemDefinition manufactoredItemDefinition;

    private Integer packageSize;

    private UnitOfPresentation unitOfPresentation;
}


