package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.PackagedMedicinalProductRepository;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.MedicationKnowledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MedicationKnowledgeResourceProvider implements IResourceProvider {

    private static Logger LOG = LoggerFactory.getLogger(MedicationKnowledgeResourceProvider.class);

    final private PackagedMedicinalProductRepository packagedMedicinalProductRepository;

    @Autowired
    public MedicationKnowledgeResourceProvider(PackagedMedicinalProductRepository PackagedMedicinalProductRepository) {
        this.packagedMedicinalProductRepository = PackagedMedicinalProductRepository;
    }


    @Override
    public Class<MedicationKnowledge> getResourceType() {
        return MedicationKnowledge.class;
    }

    @Read()
    public MedicationKnowledge getResourceById(@IdParam IdType id) {
        Optional<PackagedMedicinalProduct> result = packagedMedicinalProductRepository.findById(id.getIdPartAsLong());
        //return result.map(MedicationKnowledgeResourceProvider::medicationKnowledgeFromEntity).orElse(null);
        return null;
    }

    @Search()
    public List<MedicationKnowledge> findAllResources() {
        ArrayList<MedicationKnowledge> medicationKnowledges = new ArrayList<>();

//        for (it.datawizard.unicom.unicombackend.jpa.entity.MedicationKnowledge medicationKnowledge: PackagedMedicinalProductRepository.findAll()) {
//            medicationKnowledges.add(medicationKnowledgeFromEntity(medicationKnowledge));
//        }

        return medicationKnowledges;
    }

    public static MedicationKnowledge medicationKnowledgeFromEntity(PackagedMedicinalProduct entityMedicationKnowledge) {
        MedicationKnowledge medicationKnowledge = new MedicationKnowledge();

        medicationKnowledge.setId(entityMedicationKnowledge.getId().toString());


        //TODO: add CodeableConcept code
        //TODO: add CodeableConcept status
        //TODO: add CodeableConcept author
        //TODO: add CodeableConcept intendedJurisdiction
        //TODO: add CodeableConcept name
        //TODO: add CodeableConcept relatedMedicationKnowledge
        //TODO: add CodeableConcept associatedMedication
        //TODO: add CodeableConcept productType
        //TODO: add CodeableConcept monograph
        //TODO: add CodeableConcept preparationInstruction
        //TODO: add CodeableConcept cost
        //TODO: add CodeableConcept monitoringProgram
        //TODO: add CodeableConcept indicationGuideline
        //TODO: add CodeableConcept medicineClassification
        //TODO: add CodeableConcept packaging
        //TODO: add CodeableConcept clinicalUseIssue
        //TODO: add CodeableConcept storageGuideline
        //TODO: add CodeableConcept regulatory
        //TODO: add CodeableConcept definitional
        return medicationKnowledge;
    }




}
