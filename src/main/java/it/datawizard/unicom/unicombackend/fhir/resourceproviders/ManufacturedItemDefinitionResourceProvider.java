package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.repository.ManufacturedItemRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ManufacturedItemDefinitionResourceProvider implements IResourceProvider {
    private final ManufacturedItemRepository manufacturedItemRepository;

    @Autowired
    public ManufacturedItemDefinitionResourceProvider(ManufacturedItemRepository manufacturedItemRepository) {
        this.manufacturedItemRepository = manufacturedItemRepository;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return ManufacturedItemDefinition.class;
    }

    @Read()
    @Transactional
    public ManufacturedItemDefinition getResourceById(@IdParam IdType id) {
        return manufacturedItemRepository.findById(id.getIdPartAsLong())
                .map(ManufacturedItemDefinitionResourceProvider::manufacturedItemDefinitionFromEntity)
                .orElse(null);
    }

    @Search
    @Transactional
    public List<ManufacturedItemDefinition> findAllResources() {
        return manufacturedItemRepository.findAll().stream()
                .map(ManufacturedItemDefinitionResourceProvider::manufacturedItemDefinitionFromEntity)
                .toList();
    }

    public static ManufacturedItemDefinition manufacturedItemDefinitionFromEntity(ManufacturedItem manufacturedItemEntity) {
        ManufacturedItemDefinition manufacturedItemDefinition = new ManufacturedItemDefinition();

        // id
        manufacturedItemDefinition.setId(manufacturedItemEntity.getId().toString());

        // manufacturedDoseForm
        CodeableConcept manufacturedDoseFormCodeableConcept = new CodeableConcept();
        manufacturedDoseFormCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000004",
                manufacturedItemEntity.getManufacturedDoseForm().getCode(),
                manufacturedItemEntity.getManufacturedDoseForm().getTerm()
        );
        manufacturedItemDefinition.setManufacturedDoseForm(manufacturedDoseFormCodeableConcept);

        // unitOfPresentation
        CodeableConcept unitOfPresentationCodeableConcept = new CodeableConcept();
        unitOfPresentationCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000014",
                manufacturedItemEntity.getUnitOfPresentation().getCode(),
                manufacturedItemEntity.getUnitOfPresentation().getTerm()
        );
        manufacturedItemDefinition.setUnitOfPresentation(unitOfPresentationCodeableConcept);

        return manufacturedItemDefinition;
    }
}
