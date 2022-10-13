package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.AtcCode;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MedicinalProductDefinitionResourceProvider implements IResourceProvider {
    private final MedicinalProductRepository medicinalProductRepository;

    @Autowired
    public MedicinalProductDefinitionResourceProvider(MedicinalProductRepository medicinalProductRepository) {
        this.medicinalProductRepository = medicinalProductRepository;
    }


    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return MedicinalProductDefinition.class;
    }

    @Read()
    @Transactional
    public MedicinalProductDefinition getResourceById(@IdParam IdType id) {
        Optional<MedicinalProduct> result = medicinalProductRepository.findById(id.getIdPartAsLong());
        return result.map(MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity).orElse(null);
    }

    @Search
    @Transactional
    public List<MedicinalProductDefinition> findAllResources() {
        ArrayList<MedicinalProductDefinition> resources = new ArrayList<>();
        for (MedicinalProduct medicinalProduct: medicinalProductRepository.findAll()) {
            resources.add(medicinalProductDefinitionFromEntity(medicinalProduct));
        }
        return resources;
    }

    @Search
    @Transactional
    public MedicinalProductDefinition findByIdentifier(@RequiredParam(name = MedicinalProductDefinition.SP_IDENTIFIER) StringParam identifier) {
        MedicinalProduct medicinalProduct = medicinalProductRepository.findByMpId(identifier.getValue());

        if (medicinalProduct == null)
            return null;

        return medicinalProductDefinitionFromEntity(medicinalProduct);
    }

    @Search
    @Transactional
    public List<MedicinalProductDefinition> findByClassification(@RequiredParam(name = MedicinalProductDefinition.SP_PRODUCT_CLASSIFICATION) StringParam classification) {
        List<MedicinalProduct> medicinalProducts = medicinalProductRepository.findByAtcCodesIn(Collections.singleton(classification.getValue()));

        if (medicinalProducts == null)
            return null;

        return medicinalProducts.stream().map((MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity)).collect(Collectors.toList());
    }


    @Search
    @Transactional
    public MedicinalProductDefinition findByName(@RequiredParam(name = MedicinalProductDefinition.SP_NAME) StringParam name) {
        MedicinalProduct medicinalProduct = medicinalProductRepository.findByFullName(name.getValue());

        if (medicinalProduct == null)
            return null;

        return medicinalProductDefinitionFromEntity(medicinalProduct);
    }
    public static MedicinalProductDefinition medicinalProductDefinitionFromEntity(MedicinalProduct medicinalProductEntity) {
        MedicinalProductDefinition medicinalProductDefinition = new MedicinalProductDefinition();
        medicinalProductDefinition.setId(medicinalProductEntity.getId().toString());

        //Identifier
        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/mpId");
        identifier.setValue(medicinalProductEntity.getMpId());
        identifiers.add(identifier);
        medicinalProductDefinition.setIdentifier(identifiers);

        //Type
        CodeableConcept typeCodeableConcept = new CodeableConcept();
        typeCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000000002",
                "200000025916",
                "Authorised Medicinal Product"
        );
        medicinalProductDefinition.setType(typeCodeableConcept);

        //Country
        // TODO download all possible values and put them inside an entity
        //      * http://spor.ema.europa.eu/rmswi/#/lists/100000000002/terms
        CodeableConcept countryCodeableConcept = new CodeableConcept();
        countryCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000000002",
                "TODO use appropriate code",
                "TODO use appropriate term"
        );

        //Name
        ArrayList<MedicinalProductDefinition.MedicinalProductDefinitionNameComponent> names = new ArrayList<>();
        MedicinalProductDefinition.MedicinalProductDefinitionNameComponent name = new MedicinalProductDefinition.MedicinalProductDefinitionNameComponent();
        name.setProductName(medicinalProductEntity.getFullName());
        names.add(name);
        medicinalProductDefinition.setName(names);

        //Classification
        ArrayList<CodeableConcept> codes = new ArrayList<>();
        for (AtcCode code : medicinalProductEntity.getAtcCodes()) {
            CodeableConcept codeCodeableConcept = new CodeableConcept();
            codeCodeableConcept.addCoding("https://spor.ema.europa.eu/v1/lists/100000093533",code.getAtcCode(), code.toString());
            codes.add(codeCodeableConcept);
        }
        medicinalProductDefinition.setClassification(codes);

        return medicinalProductDefinition;
    }


}
