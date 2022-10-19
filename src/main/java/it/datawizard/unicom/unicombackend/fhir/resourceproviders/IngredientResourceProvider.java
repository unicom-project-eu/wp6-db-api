package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class IngredientResourceProvider implements IResourceProvider {
    private static Logger LOG = LoggerFactory.getLogger(IngredientResourceProvider.class);

    @Override
    public Class<? extends IBaseResource> getResourceType() { return Ingredient.class; }

    final private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientResourceProvider(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

//    @Search
//    @Transactional
//    public List<Ingredient> findAllResources() {
//        ArrayList<Ingredient> substances = new ArrayList<>();
//
//        for (it.datawizard.unicom.unicombackend.jpa.entity.Ingredient substanceWithRolePai: ingredientRepository.findAll()) {
//            substances.add(ingredientFromEntity(substanceWithRolePai));
//        }
//
//        return substances;
//    }

    @Search
    @Transactional
    public IBundleProvider findAllResources() {
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)ingredientRepository.findAll(PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                int pageSize = theToIndex-theFromIndex;
                int currentPageIndex = theFromIndex/pageSize;
                Page<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> allIngredients = ingredientRepository.findAll(PageRequest.of(currentPageIndex,pageSize));
                return allIngredients.stream()
                        .map(IngredientResourceProvider::ingredientFromEntity).collect(Collectors.toList());
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

    @Search
    @Transactional
    public Ingredient findByIngredientCode(@RequiredParam(name = Substance.SP_CODE) StringParam code) {
        it.datawizard.unicom.unicombackend.jpa.entity.Ingredient substanceWithRolePai = ingredientRepository.findBySubstance_SubstanceCode(code.getValue());

        if (substanceWithRolePai == null)
            return null;

        return ingredientFromEntity(substanceWithRolePai);
    }

    @Read
    @Transactional
    public Ingredient getResourceById(@IdParam IdType id) {
        Optional<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> result = ingredientRepository.findById(id.getIdPartAsLong());
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
             entityOuterPackageItem = entityOuterPackageItem.getParentPackageItem()) {}

        if (entityOuterPackageItem == null) {
            return  ingredient;
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


}
