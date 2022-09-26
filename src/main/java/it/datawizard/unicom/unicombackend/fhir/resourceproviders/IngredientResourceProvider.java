package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Search
    public List<Ingredient> findAllResources() {
        ArrayList<Ingredient> substances = new ArrayList<>();

        for (it.datawizard.unicom.unicombackend.jpa.entity.Ingredient substanceWithRolePai: ingredientRepository.findAll()) {
            substances.add(ingredientFromEntity(substanceWithRolePai));
        }

        return substances;
    }

    @Search
    public Ingredient findByIngredientCode(@RequiredParam(name = Substance.SP_CODE) StringParam code) {
        it.datawizard.unicom.unicombackend.jpa.entity.Ingredient substanceWithRolePai = ingredientRepository.findByReferenceSubstance(code.getValue());

        if (substanceWithRolePai == null)
            return null;

        return ingredientFromEntity(substanceWithRolePai);
    }

    @Read
    public Ingredient getResourceById(@IdParam IdType id) {
        Optional<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> result = ingredientRepository.findById(id.getIdPartAsLong());
        return result.map(IngredientResourceProvider::ingredientFromEntity).orElse(null);
    }

    public static Ingredient ingredientFromEntity(it.datawizard.unicom.unicombackend.jpa.entity.Ingredient entityIngredient) {
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
                entityIngredient.getReferenceSubstance(),
                entityIngredient.getSubstance()
        );
        codeableReference.setConcept(substanceCodeableConcept);
        ingredientSubstanceComponent.setCode(codeableReference);
        ingredient.setSubstance(ingredientSubstanceComponent);

        return ingredient;
    }
}
