package it.datawizard.unicom.unicombackend.jackson.idresolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmPackageItemType;

public class EdqmPackageItemTypeIdResolver implements ObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey idKey, Object o) {

    }

    @Override
    public EdqmPackageItemType resolveId(ObjectIdGenerator.IdKey idKey) {
        EdqmPackageItemType edqmPackageItemType = new EdqmPackageItemType();
        edqmPackageItemType.setCode((String) idKey.key);
        return edqmPackageItemType;
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
