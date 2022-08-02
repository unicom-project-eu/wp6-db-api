package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.UnitOfPresentation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class ProductContainsItem {
    @Embeddable
    @Getter
    @Setter
    public static class Key implements Serializable {
        private long medicinalProductDefinitionId;
        private long manufactoredItemDefinitionId;
    }

    @EmbeddedId
    private ProductContainsItem.Key id;

    @ManyToOne
    @MapsId("medicinalProductDefinitionId")
    private MedicinalProductDefinition medicinalProductDefinition;

    @ManyToOne
    @MapsId("manufactoredItemDefinitionId")
    private ManufactoredItemDefinition manufactoredItemDefinition;

    private Integer packageSize;
    private UnitOfPresentation unitOfPresentation;
}


