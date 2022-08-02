package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.UnitOfPresentation;

import javax.persistence.*;

@Entity
public class ProductContainsItem {
    @EmbeddedId
    ProductContainsItemKey id;

    @ManyToOne
    @MapsId("medicinalProductDefinitionId")
    MedicinalProductDefinition medicinalProductDefinition;

    @ManyToOne
    @MapsId("manufactoredItemDefinitionId")
    ManufactoredItemDefinition manufactoredItemDefinition;

    private Integer packageSize;

    private UnitOfPresentation unitOfPresentation;
}


