package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmRouteOfAdministration;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AdministrableProductDefinitionResourceProvider implements IResourceProvider {
    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;

    @Override
    public Class<AdministrableProductDefinition> getResourceType() {
        return AdministrableProductDefinition.class;
    }

    @Autowired
    public AdministrableProductDefinitionResourceProvider(PharmaceuticalProductRepository pharmaceuticalProductRepository) {
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
    }

    @Read()
    @Transactional
    public AdministrableProductDefinition getResourceById(@IdParam IdType id) {
        return administrableProductDefinitionFromEntity(
                pharmaceuticalProductRepository.findById(id.getIdPartAsLong()).orElse(null)
        );
    }

    @Search
    @Transactional
    public List<AdministrableProductDefinition> findAllResources() {
        return pharmaceuticalProductRepository.findAll().stream().map(
                AdministrableProductDefinitionResourceProvider::administrableProductDefinitionFromEntity
        ).toList();
    }

    public static AdministrableProductDefinition administrableProductDefinitionFromEntity(PharmaceuticalProduct pharmaceuticalProductEntity)  {
        AdministrableProductDefinition administrableProductDefinition = new AdministrableProductDefinition();

        // id
        administrableProductDefinition.setId(pharmaceuticalProductEntity.getId().toString());

        // Identifier
        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/phpId");
        identifier.setValue(pharmaceuticalProductEntity.getIdmpPhpId());
        identifiers.add(identifier);
        administrableProductDefinition.setIdentifier(identifiers);

        // administrableDoseForm
        CodeableConcept administrableDoseFormCcodeableConcept = new CodeableConcept();
        administrableDoseFormCcodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000004",
                pharmaceuticalProductEntity.getAdministrableDoseForm().getCode(),
                pharmaceuticalProductEntity.getAdministrableDoseForm().getTerm()
        );
        administrableProductDefinition.setAdministrableDoseForm(administrableDoseFormCcodeableConcept);

        // unitOfPresentation
        CodeableConcept unitOfPresentationCodeableConcept = new CodeableConcept();
        unitOfPresentationCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000014",
                pharmaceuticalProductEntity.getUnitOfPresentation().getCode(),
                pharmaceuticalProductEntity.getUnitOfPresentation().getTerm()
        );
        administrableProductDefinition.setUnitOfPresentation(unitOfPresentationCodeableConcept);

        // rotesOfAdministration
        for (EdqmRouteOfAdministration edqmRouteOfAdministration : pharmaceuticalProductEntity.getRoutesOfAdministration()) {
            CodeableConcept routeOfAdministrationCodeableConcept = new CodeableConcept();
            routeOfAdministrationCodeableConcept.addCoding(
                    "https://spor.ema.europa.eu/v1/lists/100000073345",
                    edqmRouteOfAdministration.getCode(),
                    edqmRouteOfAdministration.getTerm()
            );

            AdministrableProductDefinition.AdministrableProductDefinitionRouteOfAdministrationComponent
                    fhirRouteOfAdministration = new AdministrableProductDefinition
                        .AdministrableProductDefinitionRouteOfAdministrationComponent(routeOfAdministrationCodeableConcept);

            administrableProductDefinition.addRouteOfAdministration(fhirRouteOfAdministration);
        }

        // Ingredient
        for (Ingredient ingredientEntity : pharmaceuticalProductEntity.getIngredients()) {
            Reference reference = new Reference();
            reference.setResource(
                    IngredientResourceProvider.ingredientFromEntity(ingredientEntity)
            );

            CodeableReference ingredientCodeableReference = new CodeableReference(reference);
            administrableProductDefinition.addIngredient(ingredientCodeableReference.getConcept());
        }

        return administrableProductDefinition;
    }
}
