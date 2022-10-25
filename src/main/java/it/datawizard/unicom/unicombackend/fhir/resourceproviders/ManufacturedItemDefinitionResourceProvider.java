package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.ManufacturedItemRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.ManufacturedItemSpecifications;
import it.datawizard.unicom.unicombackend.jpa.specification.MedicinalProductSpecifications;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Component
public class ManufacturedItemDefinitionResourceProvider implements IResourceProvider {
    private final ManufacturedItemRepository manufacturedItemRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;


    @Autowired
    public ManufacturedItemDefinitionResourceProvider(ManufacturedItemRepository manufacturedItemRepository, PlatformTransactionManager platformTransactionManager) {
        this.manufacturedItemRepository = manufacturedItemRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return ManufacturedItemDefinition.class;
    }

    @Read()
    @Transactional
    public ManufacturedItemDefinition getResourceById(RequestDetails requestDetails, @IdParam IdType id) {
        return manufacturedItemRepository.findByIdAndPackageItem_RootPackagedMedicinalProduct_MedicinalProduct_Country(id.getIdPartAsLong(), requestDetails.getTenantId())
                .map(ManufacturedItemDefinitionResourceProvider::manufacturedItemDefinitionFromEntity)
                .orElse(null);
    }

    @Search
    @Transactional
    public IBundleProvider findResources(RequestDetails requestDetails, @OptionalParam(name = ManufacturedItemDefinition.SP_DOSE_FORM) StringParam manufacturedDoseForm, @OptionalParam(name = ManufacturedItemDefinition.SP_INGREDIENT) StringParam ingredient) {
        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        Specification<ManufacturedItem> specification = Specification
                .where(tenantId != null ? ManufacturedItemSpecifications.isCountryEqualTo(tenantId) : null)
                .and(manufacturedDoseForm != null ? ManufacturedItemSpecifications.isManufacturedDoseFormEqualTo(manufacturedDoseForm.getValue()) : null)
                .and(ingredient != null ? ManufacturedItemSpecifications.ingredientsContains(ingredient.getValue()): null);

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)manufacturedItemRepository.findAll(specification,PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<ManufacturedItem> allMedicinalProducts = manufacturedItemRepository.findAll(specification, PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allMedicinalProducts.stream()
                            .map(ManufacturedItemDefinitionResourceProvider::manufacturedItemDefinitionFromEntity)
                            .toList());
                    return null;
                });

                return results;
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
