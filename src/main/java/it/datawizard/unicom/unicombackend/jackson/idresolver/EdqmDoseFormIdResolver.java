package it.datawizard.unicom.unicombackend.jackson.idresolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;

public class EdqmDoseFormIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {

    }

    @Override
    public EdqmDoseForm resolveId(ObjectIdGenerator.IdKey idKey) {
        EdqmDoseForm edqmDoseForm = new EdqmDoseForm();
        edqmDoseForm.setCode(idKey.toString());
        return edqmDoseForm;
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
