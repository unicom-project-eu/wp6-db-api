package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.IngredientSpecifications;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class IngredientResourceProvider implements IResourceProvider {
    private static Logger LOG = LoggerFactory.getLogger(IngredientResourceProvider.class);

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Ingredient.class;
    }

    final private IngredientRepository ingredientRepository;
    final private PlatformTransactionManager platformTransactionManager;
    final private TransactionTemplate transactionTemplate;

    @Autowired
    public IngredientResourceProvider(IngredientRepository ingredientRepository, PlatformTransactionManager platformTransactionManager) {
        this.ingredientRepository = ingredientRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Search
    @Transactional
    public IBundleProvider findResources(
            RequestDetails requestDetails,
            @OptionalParam(name = Ingredient.SP_FOR) ReferenceParam forReference,
            @OptionalParam(name = Ingredient.SP_FUNCTION) StringParam function,
            @OptionalParam(name = Ingredient.SP_IDENTIFIER) StringParam identifier,
            @OptionalParam(name = Ingredient.SP_MANUFACTURER) ReferenceParam manufacturerReference,
            @OptionalParam(name = Ingredient.SP_ROLE) StringParam role,
            @OptionalParam(name = Ingredient.SP_SUBSTANCE) ReferenceParam substanceReference,
            @OptionalParam(name = Ingredient.SP_SUBSTANCE_CODE) StringParam substanceCode,
            @OptionalParam(name = Ingredient.SP_SUBSTANCE_DEFINITION) ReferenceParam substanceDefinitionReference) {
        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        handleReferenceParamsExceptions(forReference, manufacturerReference, substanceReference, substanceDefinitionReference);

        Specification<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> specification = Specification
                .where(tenantId != null ? IngredientSpecifications.isCountryEqualTo(tenantId) : null)
                .and((forReference != null && forReference.getResourceType().equals("ManufacturedItemDefinition")) ? IngredientSpecifications.isManufacturedItemEqualTo(forReference.getIdPart()) : null)
                .and((forReference != null && forReference.getResourceType().equals("MedicinalProductDefinition")) ? IngredientSpecifications.isMedicinalProductEqualTo(forReference.getIdPart()) : null)
                .and((forReference != null && forReference.getResourceType().equals("AdministrableProductDefinition")) ? IngredientSpecifications.isPharmaceuticalProductEqualTo(forReference.getIdPart()) : null)
                .and(role != null ? IngredientSpecifications.isRoleEqualTo(role.getValue()) : null)
                .and(substanceReference != null ? IngredientSpecifications.isSubstanceCodeEqualTo(substanceReference.getIdPart()) : null)
                .and(substanceCode != null ? IngredientSpecifications.isSubstanceCodeEqualTo(substanceCode.getValue()) : null)
                .and(substanceDefinitionReference != null ? IngredientSpecifications.isSubstanceCodeEqualTo(substanceDefinitionReference.getIdPart()) : null);

        return new IBundleProvider() {
            final boolean shouldReturnEmptyResult = Stream.of(function, identifier,manufacturerReference).filter(bp -> bp != null).count() > 0;

            @Override
            public Integer size() {
                return shouldReturnEmptyResult? 0 : (int) ingredientRepository.findAll(specification, PageRequest.of(1, 1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex - theFromIndex;
                final int currentPageIndex = theFromIndex / pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                if (!shouldReturnEmptyResult) {
                    transactionTemplate.execute(status -> {
                        Page<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> allMedicinalProducts = ingredientRepository.findAll(specification, PageRequest.of(currentPageIndex, pageSize));
                        results.addAll(allMedicinalProducts.stream()
                                .map(IngredientResourceProvider::ingredientFromEntity)
                                .toList());
                        return null;
                    });
                }

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

    @Read
    @Transactional
    public Ingredient getResourceById(RequestDetails requestDetails, @IdParam IdType id) {
        Optional<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> result = ingredientRepository.findByIdAndManufacturedItem_PackageItem_RootPackagedMedicinalProduct_MedicinalProduct_Country(id.getIdPartAsLong(), requestDetails.getTenantId());
        return result.map(IngredientResourceProvider::ingredientFromEntity).orElse(null);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    public static Ingredient ingredientFromEntity(
            it.datawizard.unicom.unicombackend.jpa.entity.Ingredient entityIngredient) {
        Ingredient ingredient = new Ingredient();

        ingredient.setId(entityIngredient.getId().toString());

        // Role: we only deal with substances with the role of precise active ingredient
        CodeableConcept roleCodeableConcept = new CodeableConcept();
        roleCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000072050",
                "100000072072",
                "active"
        );
        ingredient.setRole(roleCodeableConcept);

        // Substance
        Ingredient.IngredientSubstanceComponent ingredientSubstanceComponent = new Ingredient.IngredientSubstanceComponent();
        CodeableReference codeableReference = new CodeableReference();
        CodeableConcept substanceCodeableConcept = new CodeableConcept();
        substanceCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v2/SubstanceDefinition",
                entityIngredient.getSubstance().getSubstanceCode(),
                entityIngredient.getSubstance().getSubstanceName()
        );
        codeableReference.setConcept(substanceCodeableConcept);
        ingredientSubstanceComponent.setCode(codeableReference);

        // strength
        Ingredient.IngredientSubstanceStrengthComponent strength = ingredientSubstanceComponent.addStrength();
        Ingredient.IngredientSubstanceStrengthReferenceStrengthComponent referenceStrength = strength.addReferenceStrength();

        // referenceStrength
        Quantity numeratorQuantity = new Quantity();
        numeratorQuantity.setSystem("https://spor.ema.europa.eu/v1/lists/100000110633");
        // TODO finish this
//        referenceStrength.setSubstance(
//
//        );


        ingredient.setSubstance(ingredientSubstanceComponent);

        // for ManufacturedItem
        ManufacturedItem entityManufacturedItem = entityIngredient.getManufacturedItem();
        ingredient.addFor(new Reference(
                ManufacturedItemDefinitionResourceProvider.manufacturedItemDefinitionFromEntity(
                        entityManufacturedItem
                )
        ));

        // for MedicinalProductDefinition
        PackageItem entityOuterPackageItem;
        for (entityOuterPackageItem = entityManufacturedItem.getPackageItem();
             entityOuterPackageItem != null
                     && entityOuterPackageItem.getParentPackageItem() != null
                     && entityOuterPackageItem.getPackagedMedicinalProduct() == null;
             entityOuterPackageItem = entityOuterPackageItem.getParentPackageItem()) {
        }

        if (entityOuterPackageItem == null) {
            return ingredient;
        }

        MedicinalProduct entityMedicinalProduct = entityOuterPackageItem.getPackagedMedicinalProduct().getMedicinalProduct();
        ingredient.addFor(new Reference(
                MedicinalProductDefinitionResourceProvider.medicinalProductDefinitionFromEntity(
                        entityMedicinalProduct
                )
        ));

        // for AdministrableProductDefinition
        ingredient.addFor(new Reference(
                AdministrableProductDefinitionResourceProvider.administrableProductDefinitionFromEntity(
                        entityMedicinalProduct.getPharmaceuticalProduct()
                )
        ));

        return ingredient;
    }

    private void handleReferenceParamsExceptions(ReferenceParam forReference, ReferenceParam manufacturerReference,ReferenceParam substanceReference,ReferenceParam substanceDefinitionReference) {
        if (forReference != null && forReference.hasResourceType()) {
            String forReferenceResourceType = forReference.getResourceType();
            if (Stream.of("AdministrableProductDefinition", "ManufacturedItemDefinition", "MedicinalProductDefinition").noneMatch(s -> s.equals(forReferenceResourceType))) {
                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'for': " + forReferenceResourceType);
            }
        }

        if (manufacturerReference != null && manufacturerReference.hasResourceType()) {
            String manufacturerReferenceResourceType = manufacturerReference.getResourceType();
            if (!"Manufacturer".equals(manufacturerReferenceResourceType)) {
                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'manufacturer': " + manufacturerReferenceResourceType);
            }
        }

        if (substanceReference != null && substanceReference.hasResourceType()) {
            String substanceReferenceResourceType = substanceReference.getResourceType();
            if (!"Substance".equals(substanceReferenceResourceType)) {
                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'substance': " + substanceReferenceResourceType);
            }
        }

        if (substanceDefinitionReference != null && substanceDefinitionReference.hasResourceType()) {
            String substanceDefinitionReferenceResourceType = substanceDefinitionReference.getResourceType();
            if (!"SubstanceDefinition".equals(substanceDefinitionReferenceResourceType)) {
                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'substanceDefinition': " + substanceDefinitionReferenceResourceType);
            }
        }
    }


}
