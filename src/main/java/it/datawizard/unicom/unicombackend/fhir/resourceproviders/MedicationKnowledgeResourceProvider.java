package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationKnowledge;
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
        return null;
    }

    @Search()
    public List<MedicationKnowledge> getAllResources() {
        ArrayList<MedicationKnowledge> result = new ArrayList<>();
        return result;
    }
}
