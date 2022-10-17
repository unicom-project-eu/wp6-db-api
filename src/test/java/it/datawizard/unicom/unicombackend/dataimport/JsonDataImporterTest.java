package it.datawizard.unicom.unicombackend.dataimport;

import it.datawizard.unicom.unicombackend.UnicomApplication;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.StrengthRepository;
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
                      "packSize": 98,
                      "packageItems": [
                        {
                          "type": null,
                          "packageItemQuantity": 98,
                          "manufacturedItems": [
                            {
                              "manufacturedDoseForm": "10210000",
                              "unitOfPresentation": "15012000",
                              "manufacturedItemQuantity": 98.0,
                              "volumeUnit": null,
                              "ingredients": [
                                {
                                  "role": "100000072072",
                                  "substance": {
                                    "substanceCode": "100000090079"
                                  },
                                  "referenceStrength": {
                                    "concentrationNumeratorValue": 6.93,
                                    "concentrationDenominatorValue": null,
                                    "concentrationNumeratorUnit": null,
                                    "concentrationDenominatorUnit": null,
                                    "presentationNumeratorValue": null,
                                    "presentationDenominatorValue": null,
                                    "presentationNumeratorUnit": null,
                                    "presentationDenominatorUnit": null
                                  },
                                  "strength": {
                                    "concentrationNumeratorValue": null,
                                    "concentrationDenominatorValue": null,
                                    "concentrationNumeratorUnit": null,
                                    "concentrationDenominatorUnit": null,
                                    "presentationNumeratorValue": 5.0,
                                    "presentationDenominatorValue": null,
                                    "presentationNumeratorUnit": null,
                                    "presentationDenominatorUnit": null
                                  }
                                }
                              ]
                            }
                          ],
                          "parentPackageItem": null,
                          "childrenPackageItems": []
                        }
                      ],
                      "medicinalProduct": {
                        "mpId": null,
                        "fullName": "Amlor harde caps. 5 mg",
                        "atcCodes": [
                          "C08CA01"
                        ],
                        "authorizedPharmaceuticalDoseForm": "10210000",
                        "marketingAuthorizationHolderCode": null,
                        "marketingAuthorizationHolderLabel": null,
                        "country": "BEL",
                        "pharmaceuticalProduct": {
                          "administrableDoseForm": "10210000",
                          "unitOfPresentation": "15012000",
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
    void saveParsedPackagedMedicinalProduct() throws IOException {
        ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts = jsonDataImporter
            .parseDataJsonString(packagedMedicinalProductJsonString);

        jsonDataImporter.saveParsedPackagedMedicinalProducts(packagedMedicinalProducts);
    }
}