package it.datawizard.unicom.unicombackend.jackson.idresolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmUnitOfPresentation;

public class SubstanceIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {

    }

    @Override
    public Substance resolveId(ObjectIdGenerator.IdKey idKey) {
        Substance substance = new Substance();
        substance.setSubstanceCode((String) idKey.key);
        return substance;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object o) {
        return this;
    }

    @Override
    public boolean canUseFor(ObjectIdResolver objectIdResolver) {
        return false;
    }
}
