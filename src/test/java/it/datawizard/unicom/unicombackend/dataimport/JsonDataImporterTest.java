package it.datawizard.unicom.unicombackend.dataimport;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.datawizard.unicom.unicombackend.elasticsearch.repository.MedicinalProductElasticsearchRepository;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class JsonDataImporterTest {
    private final Logger LOG = LoggerFactory.getLogger(JsonDataImporterTest.class);

    @Autowired
    private MedicinalProductRepository medicinalProductRepository;

    @MockBean
    private UnicomFHIRServlet unicomFHIRServlet;

    @MockBean
    private MedicinalProductElasticsearchRepository medicinalProductElasticsearchRepository;

    @Test
    void importDataJsonString() throws IOException {

    }

    @Test
    void deserializePackagedMedicinalProduct() throws IOException {
        final String jsonData = """
                  {
                    "pcId": null,
                    "packSize": 30,
                    "packageItems": [
                      {
                        "type": null,
                        "packageItemQuantity": 30,
                        "manufacturedItems": [
                          {
                            "manufacturedDoseForm": "10219000",
                            "unitOfPresentation": "15054000",
                            "manufacturedItemQuantity": 30.0,
                            "volumeUnit": null
                          }
                        ],
                        "parentPackageItem": null,
                        "childrenPackageItems": []
                      }
                    ],
                    "medicinalProduct": {
                      "mpId": null,
                      "fullName": "Amlodipin AB tabl. 10 mg",
                      "atcCodes": [
                        "C08CA01"
                      ],
                      "authorizedPharmaceuticalDoseForm": "10219000",
                      "marketingAuthorizationHolderCode": null,
                      "marketingAuthorizationHolderLabel": null,
                      "country": "BEL",
                      "pharmaceuticalProduct": {
                        "administrableDoseForm": "10219000",
                        "unitOfPresentation": "15054000",
                        "routesOfAdministration": [
                          "20053000"
                        ],
                        "ingredients": [
                          {
                            "role": "100000072072",
                            "substance": {
                              "substanceCode": "100000090079",
                              "substanceName": "amlodipine besylate",
                              "moietyCode": "100000085259",
                              "moietyName": "amlodipine"
                            },
                            "referenceStrength": {
                              "concentrationNumeratorValue": 13.87,
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
                              "presentationNumeratorValue": 10.0,
                              "presentationDenominatorValue": null,
                              "presentationNumeratorUnit": null,
                              "presentationDenominatorUnit": null
                            }
                          }
                        ]
                      }
                    }
                  }
                  """;

        PackagedMedicinalProduct packagedMedicinalProduct = new ObjectMapper().readValue(jsonData, PackagedMedicinalProduct.class);
        LOG.info("we");
    }


    @Test
    void deserializePharmaceuticalProduct() throws IOException {
        final String jsonData = """
                {
                  "administrableDoseForm": "10219000",
                  "unitOfPresentation": "15054000",
                  "routesOfAdministration": [
                    "20053000"
                  ],
                  "ingredients": [
                    {
                      "role": "100000072072",
                      "substance": {
                        "substanceCode": "100000090079",
                        "substanceName": "amlodipine besylate",
                        "moietyCode": "100000085259",
                        "moietyName": "amlodipine"
                      },
                      "referenceStrength": {
                        "concentrationNumeratorValue": 13.87,
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
                        "presentationNumeratorValue": 10.0,
                        "presentationDenominatorValue": null,
                        "presentationNumeratorUnit": null,
                        "presentationDenominatorUnit": null
                      }
                    }
                  ]
                }
                """;

        PharmaceuticalProduct pharmaceuticalProduct = new ObjectMapper().readValue(jsonData, PharmaceuticalProduct.class);
        LOG.info("we");
    }
}