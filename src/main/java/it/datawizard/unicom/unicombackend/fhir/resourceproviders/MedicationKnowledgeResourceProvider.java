package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.MedicationKnowledge;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationKnowledgeResourceProvider implements IResourceProvider {
    @Override
    public Class<MedicationKnowledge> getResourceType() {
        return MedicationKnowledge.class;
    }

    @Read()
    public MedicationKnowledge getResourceById(@IdParam IdType id) {
        throw new NotImplementedException();
    }

    @Search()
    public List<MedicationKnowledge> getAllResources() {
        throw new NotImplementedException();
    }
}
