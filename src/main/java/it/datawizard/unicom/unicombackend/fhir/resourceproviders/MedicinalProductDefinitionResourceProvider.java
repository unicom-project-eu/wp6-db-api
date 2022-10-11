package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.MedicationKnowledge;
import org.hl7.fhir.r5.model.MedicinalProductDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MedicinalProductDefinitionResourceProvider implements IResourceProvider {

    private MedicinalProductRepository medicinalProductRepository;

    @Autowired
    public MedicinalProductDefinitionResourceProvider(MedicinalProductRepository medicinalProductRepository) {
        this.medicinalProductRepository = medicinalProductRepository;
    }


    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return MedicinalProductDefinition.class;
    }

    @Read()
    public MedicinalProductDefinition getResourceById(@IdParam IdType id) {
        return null;
    }


}
