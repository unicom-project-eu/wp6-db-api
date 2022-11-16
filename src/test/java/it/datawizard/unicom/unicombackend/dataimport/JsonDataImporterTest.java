package it.datawizard.unicom.unicombackend.dataimport;

import it.datawizard.unicom.unicombackend.UnicomApplication;
import it.datawizard.unicom.unicombackend.dataimport.exception.DataImportInvalidCodeException;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmDoseForm;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(JsonDataImporter.class)
class JsonDataImporterTest {
    private final Logger LOG = LoggerFactory.getLogger(JsonDataImporterTest.class);

    @Autowired
    private JsonDataImporter jsonDataImporter;

    @Autowired
    private MedicinalProductRepository medicinalProductRepository;

    @MockBean
    private UnicomApplication unicomApplication;

//    @MockBean
//    private MedicinalProductElasticsearchRepository medicinalProductElasticsearchRepository;

    private final String packagedMedicinalProductJsonString = """
             [
                {
                  "pcId": null,
                  "packSize": "20",
                  "packageItems": [
                    {
                      "type": "30009000",
                      "packageItemQuantity": null,
                      "manufacturedItems": [],
                      "childrenPackageItems": [
                        {
                          "type": "30007000",
                          "packageItemQuantity": null,
                          "manufacturedItems": [
                            {
                              "manufacturedDoseForm": "10219000",
                              "unitOfPresentation": "15054000",
                              "manufacturedItemQuantity": "20",
                              "volumeUnit": null,
                              "ingredients": [
                                {
                                  "primaryKey": "Acido ursodesossicolico;10219000;300 mg",
                                  "role": "100000072072",
                                  "substance": {
                                    "substanceCode": "100000092107"
                                  },
                                  "referenceStrength": {
                                    "concentrationNumeratorValue": null,
                                    "concentrationDenominatorValue": null,
                                    "concentrationNumeratorUnit": null,
                                    "concentrationDenominatorUnit": null,
                                    "presentationNumeratorValue": "300",
                                    "presentationDenominatorValue": 1,
                                    "presentationNumeratorUnit": "mg",
                                    "presentationDenominatorUnit": "Unit"
                                  },
                                  "strength": {
                                    "concentrationNumeratorValue": null,
                                    "concentrationDenominatorValue": null,
                                    "concentrationNumeratorUnit": null,
                                    "concentrationDenominatorUnit": null,
                                    "presentationNumeratorValue": null,
                                    "presentationDenominatorValue": null,
                                    "presentationNumeratorUnit": null,
                                    "presentationDenominatorUnit": null
                                  }
                                }
                              ]
                            }
                          ],
                          "childrenPackageItems": []
                        }
                      ]
                    }
                  ],
                  "medicinalProduct": {
                    "primaryKey": "Acido ursodesossicolico;10219000;300 mg;ACIDO URSODESOSSICOLICO 20;RATIOPHARM GMBH",
                    "mpId": null,
                    "fullName": "ACIDO URSODESOSSICOLICO",
                    "atcCodes": [
                      "A05AA02"
                    ],
                    "authorizedPharmaceuticalDoseForm": "10219000",
                    "marketingAuthorizationHolderCode": null,
                    "marketingAuthorizationHolderLabel": "RATIOPHARM GMBH",
                    "country": "ita",
                    "pharmaceuticalProduct": {
                      "primaryKey": "Acido ursodesossicolico;10219000;300 mg",
                      "administrableDoseForm": "10219000",
                      "unitOfPresentation": "15054000",
                      "routesOfAdministration": [
                        "20053000"
                      ]
                    }
                  }
                }
             ]""";

    @Test
    void parseDataJsonString() throws IOException {
        ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts = jsonDataImporter
                .parseDataJsonString(packagedMedicinalProductJsonString);
    }

    @Test
    void saveParsedPackagedMedicinalProduct() throws Exception {
        ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts = jsonDataImporter
            .parseDataJsonString(packagedMedicinalProductJsonString);

        jsonDataImporter.saveParsedPackagedMedicinalProducts(packagedMedicinalProducts);
    }

    @Test
    void testDataImportInvalidCodeException() throws Exception {
        ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts = jsonDataImporter
                .parseDataJsonString(packagedMedicinalProductJsonString);

        // forge wrong edqm dose form
        EdqmDoseForm wrongCodeDoseForm = new EdqmDoseForm();
        wrongCodeDoseForm.setCode("this code is clearly wrong");
        packagedMedicinalProducts.get(0).getMedicinalProduct().getPharmaceuticalProduct()
                .setAdministrableDoseForm(wrongCodeDoseForm);

        Assert.assertThrows(DataImportInvalidCodeException.class, () -> {
            jsonDataImporter.saveParsedPackagedMedicinalProducts(packagedMedicinalProducts);
        });
    }
}