package it.datawizard.unicom.unicombackend.datamodel.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class ProductContainsItemKey implements Serializable {
    private long medicinalProductDefinitionId;

    private long manufactoredItemDefinitionId;
}
