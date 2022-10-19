package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.repository.ManufacturedItemRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

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
    public IBundleProvider findAllResources() {
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)manufacturedItemRepository.findAll(PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                int pageSize = theToIndex-theFromIndex;
                int currentPageIndex = theFromIndex/pageSize;
                Page<ManufacturedItem> allManufacturedProducts = manufacturedItemRepository.findAll(PageRequest.of(currentPageIndex,pageSize));
                return allManufacturedProducts.stream()
                        .map(ManufacturedItemDefinitionResourceProvider::manufacturedItemDefinitionFromEntity).collect(Collectors.toList());
            }

            @Override
            public InstantType getPublished() {
                return searchTime;
            }

            @Override
            public Integer preferredPageSize() {
                // Typically this method just returns null
                return null;
            }

            @Override
            public String getUuid() {
                return null;
            }
        };
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
