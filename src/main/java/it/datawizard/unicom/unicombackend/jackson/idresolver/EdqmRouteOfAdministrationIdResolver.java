package it.datawizard.unicom.unicombackend.jackson.idresolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmRouteOfAdministration;

public class EdqmRouteOfAdministrationIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {

    }

    @Override
    public EdqmRouteOfAdministration resolveId(ObjectIdGenerator.IdKey idKey) {
        EdqmRouteOfAdministration edqmRouteOfAdministration = new EdqmRouteOfAdministration();
        edqmRouteOfAdministration.setCode((String) idKey.key);
        return edqmRouteOfAdministration;
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
