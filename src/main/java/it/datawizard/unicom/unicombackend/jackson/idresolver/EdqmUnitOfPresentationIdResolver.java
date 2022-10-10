package it.datawizard.unicom.unicombackend.jackson.idresolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmUnitOfPresentation;

public class EdqmUnitOfPresentationIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {

    }

    @Override
    public EdqmUnitOfPresentation resolveId(ObjectIdGenerator.IdKey idKey) {
        EdqmUnitOfPresentation edqmUnitOfPresentation = new EdqmUnitOfPresentation();
        edqmUnitOfPresentation.setCode((String) idKey.key);
        return edqmUnitOfPresentation;
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
