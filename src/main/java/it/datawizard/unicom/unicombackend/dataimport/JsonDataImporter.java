package it.datawizard.unicom.unicombackend.dataimport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.datawizard.unicom.unicombackend.dataimport.exception.DataImportException;
import it.datawizard.unicom.unicombackend.dataimport.exception.DataImportInvalidCodeException;
import it.datawizard.unicom.unicombackend.dataimport.exception.DataImportSaveException;
import it.datawizard.unicom.unicombackend.jpa.entity.*;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmConcept;
import it.datawizard.unicom.unicombackend.jpa.repository.*;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmConceptRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmDoseFormRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmRouteOfAdministrationRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.edqm.EdqmUnitOfPresentationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class JsonDataImporter {
    private final static Logger LOG = LoggerFactory.getLogger(JsonDataImporter.class);
    private final ManufacturedItemRepository manufacturedItemRepository;
    private final PackageItemRepository packageItemRepository;
    private final PackagedMedicinalProductRepository packagedMedicinalProductRepository;
    private final MedicinalProductRepository medicinalProductRepository;
    private final AtcCodeRepository atcCodeRepository;
    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;
    private final EdqmDoseFormRepository edqmDoseFormRepository;
    private final EdqmUnitOfPresentationRepository edqmUnitOfPresentationRepository;
    private final EdqmRouteOfAdministrationRepository edqmRouteOfAdministrationRepository;
    private final IngredientRepository ingredientRepository;
    private final StrengthRepository strengthRepository;
    private final SubstanceRepository substanceRepository;

    @Autowired
    public JsonDataImporter(ManufacturedItemRepository manufacturedItemRepository, PackageItemRepository packageItemRepository, PackagedMedicinalProductRepository packagedMedicinalProductRepository, MedicinalProductRepository medicinalProductRepository, AtcCodeRepository atcCodeRepository, PharmaceuticalProductRepository pharmaceuticalProductRepository, EdqmDoseFormRepository edqmDoseFormRepository, EdqmUnitOfPresentationRepository edqmUnitOfPresentationRepository, EdqmRouteOfAdministrationRepository edqmRouteOfAdministrationRepository, IngredientRepository ingredientRepository, StrengthRepository strengthRepository, SubstanceRepository substanceRepository) {
        this.manufacturedItemRepository = manufacturedItemRepository;
        this.packageItemRepository = packageItemRepository;
        this.packagedMedicinalProductRepository = packagedMedicinalProductRepository;
        this.medicinalProductRepository = medicinalProductRepository;
        this.atcCodeRepository = atcCodeRepository;
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
        this.edqmDoseFormRepository = edqmDoseFormRepository;
        this.edqmUnitOfPresentationRepository = edqmUnitOfPresentationRepository;
        this.edqmRouteOfAdministrationRepository = edqmRouteOfAdministrationRepository;
        this.ingredientRepository = ingredientRepository;
        this.strengthRepository = strengthRepository;
        this.substanceRepository = substanceRepository;
    }

    public void importData(File jsonFile) throws IOException, DataImportSaveException {
        String jsonData =  Files.readString(jsonFile.toPath());
        ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts = parseDataJsonString(jsonData);
        saveParsedPackagedMedicinalProducts(packagedMedicinalProducts);
    }

    public ArrayList<PackagedMedicinalProduct> parseDataJsonString(String jsonData) throws IOException {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
                .readValue(jsonData, new TypeReference<>() {});
    }

    private <T extends EdqmConcept> T getEdqmConceptOrThrow(T concept, EdqmConceptRepository<T> repository)
            throws DataImportInvalidCodeException {
        String code = concept.getCode();
        return repository.findById(code)
                .orElseThrow(() -> new DataImportInvalidCodeException(code, concept.getClass()));
    }

    @Transactional(rollbackFor = {DataImportSaveException.class})
    protected void saveParsedPackagedMedicinalProducts(ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts) {
        for (PackagedMedicinalProduct packagedMedicinalProduct : packagedMedicinalProducts) {
            try {
                // PackagedMedicinalProduct > MedicinalProduct > PharmaceuticalProduct
                PharmaceuticalProduct pharmaceuticalProduct = packagedMedicinalProduct.getMedicinalProduct()
                        .getPharmaceuticalProduct();

                pharmaceuticalProduct = pharmaceuticalProductRepository.save(pharmaceuticalProduct);

                // PackagedMedicinalProduct > MedicinalProduct > PharmaceuticalProduct > administrableDoseForm
                if (pharmaceuticalProduct.getAdministrableDoseForm() != null) {
                    pharmaceuticalProduct.setAdministrableDoseForm(
                            getEdqmConceptOrThrow(pharmaceuticalProduct.getAdministrableDoseForm(),
                                    edqmDoseFormRepository));
                }

                // PackagedMedicinalProduct > MedicinalProduct > PharmaceuticalProduct > unitOfPresentation
                if (pharmaceuticalProduct.getUnitOfPresentation() != null) {
                    pharmaceuticalProduct.setUnitOfPresentation(
                            getEdqmConceptOrThrow(pharmaceuticalProduct.getUnitOfPresentation(),
                                    edqmUnitOfPresentationRepository));
                }

                // PackagedMedicinalProduct > MedicinalProduct > PharmaceuticalProduct > routesOfAdministration
                pharmaceuticalProduct.setRoutesOfAdministration(
                        pharmaceuticalProduct.getRoutesOfAdministration().stream().map(
                                edqmRouteOfAdministration -> getEdqmConceptOrThrow(edqmRouteOfAdministration,
                                        edqmRouteOfAdministrationRepository)
                        ).collect(Collectors.toSet())
                );

                pharmaceuticalProductRepository.save(pharmaceuticalProduct);

                // PackagedMedicinalProduct > MedicinalProduct
                MedicinalProduct medicinalProduct = packagedMedicinalProduct.getMedicinalProduct();
                medicinalProduct.setPharmaceuticalProduct(pharmaceuticalProduct);

                // PackagedMedicinalProduct > MedicinalProduct > authorizedPharmaceuticalDoseForm
                if (medicinalProduct.getAuthorizedPharmaceuticalDoseForm() != null)
                    medicinalProduct.setAuthorizedPharmaceuticalDoseForm(
                            getEdqmConceptOrThrow(medicinalProduct.getAuthorizedPharmaceuticalDoseForm(),
                                    edqmDoseFormRepository)
                    );

                medicinalProductRepository.save(medicinalProduct);

                // PackagedMedicinalProduct > MedicinalProduct > atcCodes
                medicinalProduct.setAtcCodes(
                        medicinalProduct.getAtcCodes().stream().map(atcCode -> {
                                    atcCode.setMedicinalProduct(medicinalProduct);
                                    return atcCodeRepository.save(atcCode);
                                })
                                .collect(Collectors.toSet())
                );

                // PackagedMedicinalProduct
                packagedMedicinalProduct.setMedicinalProduct(medicinalProduct);

                packagedMedicinalProductRepository.save(packagedMedicinalProduct);

                // PackagedMedicinalProduct > PackageItems
                for (PackageItem packageItem : packagedMedicinalProduct.getPackageItems()) {
                    packageItem = savePackageItem(packageItem, packagedMedicinalProduct);
                    packageItem.setPackagedMedicinalProduct(packagedMedicinalProduct);
                    packageItemRepository.save(packageItem);
                }
            }
            catch (DataImportException e) {
                throw e;
            }
            catch (Exception e) {
                throw new DataImportSaveException(e, packagedMedicinalProduct);
            }
        }
    }

    private PackageItem savePackageItem(PackageItem packageItem, PackagedMedicinalProduct rootPackagedMedicinalProduct) {
        packageItem.setRootPackagedMedicinalProduct(rootPackagedMedicinalProduct);
        packageItemRepository.save(packageItem);

        // ManufacturedItem
        packageItem.getManufacturedItems().forEach(manufacturedItem -> {
            try {
                // packageItem
                manufacturedItem.setPackageItem(packageItem);

                // manufacturedDoseForm
                if (manufacturedItem.getManufacturedDoseForm() != null) {
                    manufacturedItem.setManufacturedDoseForm(
                            getEdqmConceptOrThrow(manufacturedItem.getManufacturedDoseForm(),
                                    edqmDoseFormRepository));
                }

                // unitOfPresentation
                if (manufacturedItem.getUnitOfPresentation() != null)
                    manufacturedItem.setUnitOfPresentation(
                            getEdqmConceptOrThrow(manufacturedItem.getUnitOfPresentation(),
                                    edqmUnitOfPresentationRepository));

                manufacturedItemRepository.save(manufacturedItem);

                // ManufacturedItem > ingredients
                for (Ingredient ingredient : manufacturedItem.getIngredients()) {
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

                    ingredient.setManufacturedItem(manufacturedItem);
                    ingredientRepository.save(ingredient);
                }
            } catch (DataImportInvalidCodeException e) {
                throw new RuntimeException(e);
            }
        });

        // PackageItem > childrenPackageItems
        for (PackageItem childPackageItem : packageItem.getChildrenPackageItems()) {
            childPackageItem = savePackageItem(childPackageItem,rootPackagedMedicinalProduct);
            childPackageItem.setParentPackageItem(packageItem);
            packageItemRepository.save(childPackageItem);
        }

        return packageItem;
    }
}
