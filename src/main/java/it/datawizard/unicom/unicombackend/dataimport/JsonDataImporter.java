package it.datawizard.unicom.unicombackend.dataimport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.datawizard.unicom.unicombackend.jpa.entity.*;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.StrengthRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmDoseFormRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmRouteOfAdministrationRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmUnitOfPresentationRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class JsonDataImporter {
    private final static Logger LOG = LoggerFactory.getLogger(JsonDataImporter.class);

    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;
    private final EdqmDoseFormRepository edqmDoseFormRepository;
    private final EdqmUnitOfPresentationRepository edqmUnitOfPresentationRepository;
    private final EdqmRouteOfAdministrationRepository edqmRouteOfAdministrationRepository;
    private final IngredientRepository ingredientRepository;
    private final StrengthRepository strengthRepository;
    private final SubstanceRepository substanceRepository;

    @Autowired
    public JsonDataImporter(PharmaceuticalProductRepository pharmaceuticalProductRepository, EdqmDoseFormRepository edqmDoseFormRepository, EdqmUnitOfPresentationRepository edqmUnitOfPresentationRepository, EdqmRouteOfAdministrationRepository edqmRouteOfAdministrationRepository, IngredientRepository ingredientRepository, StrengthRepository strengthRepository, SubstanceRepository substanceRepository) {
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
        this.edqmDoseFormRepository = edqmDoseFormRepository;
        this.edqmUnitOfPresentationRepository = edqmUnitOfPresentationRepository;
        this.edqmRouteOfAdministrationRepository = edqmRouteOfAdministrationRepository;
        this.ingredientRepository = ingredientRepository;
        this.strengthRepository = strengthRepository;
        this.substanceRepository = substanceRepository;
    }

    public void importData(File jsonFile) throws IOException {
        LOG.info("Importing " + jsonFile.getPath());
    }

    public ArrayList<PackagedMedicinalProduct> parseDataJsonString(String jsonData) throws IOException {
        return new ObjectMapper().readValue(jsonData, new TypeReference<>() {});
    }

    @Transactional
    protected void saveParsedPackagedMedicinalProducts(ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts) {
        for (PackagedMedicinalProduct packagedMedicinalProduct : packagedMedicinalProducts) {
            // MedicinalProduct > PharmaceuticalProduct
            PharmaceuticalProduct pharmaceuticalProduct = packagedMedicinalProduct.getMedicinalProduct()
                    .getPharmaceuticalProduct();

            // PharmaceuticalProduct > ingredients
            for (Ingredient ingredient : pharmaceuticalProduct.getIngredients()) {
                // referenceStrength
                Strength referenceStrength = strengthRepository.save(ingredient.getReferenceStrength());
                ingredient.setReferenceStrength(referenceStrength);

                // strength
                Strength strength = strengthRepository.save(ingredient.getStrength());
                ingredient.setStrength(strength);

                // substance
                ingredient.setSubstance(
                    substanceRepository.findById(ingredient.getSubstance().getSubstanceCode()).orElseThrow()
                );

                ingredientRepository.save(ingredient);
            }

            // PharmaceuticalProduct > administrableDoseForm
            pharmaceuticalProduct.setAdministrableDoseForm(
                edqmDoseFormRepository.findById(pharmaceuticalProduct.getAdministrableDoseForm().getCode())
                        .orElseThrow()
            );

            // PharmaceuticalProduct > unitOfPresentation
            pharmaceuticalProduct.setUnitOfPresentation(
                    edqmUnitOfPresentationRepository.findById(pharmaceuticalProduct.getUnitOfPresentation().getCode())
                            .orElseThrow()
            );

            // PharmaceuticalProduct > routesOfAdministration
            pharmaceuticalProduct.setRoutesOfAdministration(
                    pharmaceuticalProduct.getRoutesOfAdministration().stream().map(
                            edqmRouteOfAdministration -> edqmRouteOfAdministrationRepository
                                    .findById(edqmRouteOfAdministration.getCode()).orElseThrow()
                    ).collect(Collectors.toSet())
            );

            pharmaceuticalProductRepository.save(pharmaceuticalProduct);

            // MedicinalProduct
            MedicinalProduct medicinalProduct = packagedMedicinalProduct.getMedicinalProduct();


        }

        throw new NotImplementedException();
    }
}
