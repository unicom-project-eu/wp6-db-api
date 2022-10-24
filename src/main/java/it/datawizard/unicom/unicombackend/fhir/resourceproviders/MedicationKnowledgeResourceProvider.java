package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.PackagedMedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class MedicationKnowledgeResourceProvider implements IResourceProvider {

    private static Logger LOG = LoggerFactory.getLogger(MedicationKnowledgeResourceProvider.class);
    final private MedicinalProductRepository medicinalProductRepository;
    final private PlatformTransactionManager platformTransactionManager;
    final private TransactionTemplate transactionTemplate;

    @Autowired
    public MedicationKnowledgeResourceProvider(MedicinalProductRepository medicinalProductRepository, PlatformTransactionManager platformTransactionManager) {
        this.medicinalProductRepository = medicinalProductRepository;
        this.platformTransactionManager = platformTransactionManager;
        transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }


    @Override
    public Class<MedicationKnowledge> getResourceType() {
        return MedicationKnowledge.class;
    }

    @Read()
    public MedicationKnowledge getResourceById(RequestDetails requestDetails, @IdParam IdType id) {
        Optional<MedicinalProduct> result = medicinalProductRepository
                .findByIdAndCountry(id.getIdPartAsLong(), requestDetails.getTenantId());
        return result.map(MedicationKnowledgeResourceProvider::medicationKnowledgeFromEntity).orElse(null);
    }

    @Search()
    public IBundleProvider findAllResources(RequestDetails requestDetails) {
        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {
            @Override
            public IPrimitiveType<Date> getPublished() {
                return searchTime;
            }

            @NotNull
            @Override
            public Integer size() {
                return (int) medicinalProductRepository
                        .findByCountry(tenantId, PageRequest.of(1, 1)).getTotalElements();
            }

            @NotNull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<MedicinalProduct> allMedicinalProducts = medicinalProductRepository
                            .findByCountry(tenantId, PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allMedicinalProducts.stream()
                            .map(MedicationKnowledgeResourceProvider::medicationKnowledgeFromEntity)
                            .toList());
                    return null;
                });

                return results;
            }

            @Nullable
            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public Integer preferredPageSize() {
                return null;
            }
        };
    }

    public static MedicationKnowledge medicationKnowledgeFromEntity(MedicinalProduct entityMedicinalProduct) {
        MedicationKnowledge medicationKnowledge = new MedicationKnowledge();

        medicationKnowledge.setId(entityMedicinalProduct.getId().toString());

        // packaging
        List<MedicationKnowledge.MedicationKnowledgePackagingComponent> packagingComponents =
                entityMedicinalProduct.getPackagedMedicinalProducts().stream().map(entityPackagedMedicinalProduct -> {
                    MedicationKnowledge.MedicationKnowledgePackagingComponent packagingComponent =
                            new MedicationKnowledge.MedicationKnowledgePackagingComponent();

                    packagingComponent.setPackagedProduct(new Reference(PackagedProductDefinitionResourceProvider
                            .packagedProductDefinitionFromEntity(entityPackagedMedicinalProduct)));

                    return packagingComponent;
                }).toList();
        medicationKnowledge.setPackaging(packagingComponents);

        // definitional
        final MedicationKnowledge.MedicationKnowledgeDefinitionalComponent definitionalComponent
                = new MedicationKnowledge.MedicationKnowledgeDefinitionalComponent();
        medicationKnowledge.setDefinitional(definitionalComponent);

        // definitional > definition
        List<Reference> definitions = new ArrayList<>();
        definitions.add(new Reference(MedicinalProductDefinitionResourceProvider
                .medicinalProductDefinitionFromEntity(entityMedicinalProduct)));
        definitionalComponent.setDefinition(definitions);

        // definitional > ingredient
        entityMedicinalProduct.getAllIngredients().forEach(ingredient -> {
            MedicationKnowledge.MedicationKnowledgeDefinitionalIngredientComponent definitionalIngredientComponent
                    = new MedicationKnowledge.MedicationKnowledgeDefinitionalIngredientComponent();
            Reference ingredientReference = new Reference(IngredientResourceProvider.ingredientFromEntity(ingredient));
            definitionalIngredientComponent.setItem(new CodeableReference(ingredientReference));
            definitionalComponent.addIngredient(definitionalIngredientComponent);
        });

        return medicationKnowledge;
    }
}
