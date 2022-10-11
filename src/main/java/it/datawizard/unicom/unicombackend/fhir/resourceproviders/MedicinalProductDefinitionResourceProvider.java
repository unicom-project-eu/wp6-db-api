package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        Optional<MedicinalProduct> result = medicinalProductRepository.findById(id.getIdPartAsLong());
        return result.map(MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity).orElse(null);
    }

    @Search
    public List<MedicinalProductDefinition> findAllResources() {
        ArrayList<MedicinalProductDefinition> resources = new ArrayList<>();
        for (MedicinalProduct medicinalProduct: medicinalProductRepository.findAll()) {
            resources.add(medicinalProductDefinitionFromEntity(medicinalProduct));
        }
        return resources;
    }
    public static MedicinalProductDefinition medicinalProductDefinitionFromEntity(MedicinalProduct medicinalProductEntity) {
        MedicinalProductDefinition medicinalProductDefinition = new MedicinalProductDefinition();
        medicinalProductDefinition.setId(medicinalProductEntity.getId().toString());

        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/mpId");
        identifier.setValue(medicinalProductEntity.getMpId());
        identifiers.add(identifier);
        medicinalProductDefinition.setIdentifier(identifiers);

        return medicinalProductDefinition;
    }


}
