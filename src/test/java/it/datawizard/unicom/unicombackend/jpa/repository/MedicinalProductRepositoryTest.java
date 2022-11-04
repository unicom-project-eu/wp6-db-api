package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.dataimport.JsonDataImporter;
import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.DispatcherServlet;

@RunWith(SpringRunner.class)
@DataJpaTest
@Getter
@Setter
public class MedicinalProductRepositoryTest {
    @Autowired
    private MedicinalProductRepository medicinalProductRepository;

    @Autowired
    private PharmaceuticalProductRepository pharmaceuticalProductRepository;

    @MockBean
    private DispatcherServlet dispatcherServlet;

    @MockBean
    private UnicomFHIRServlet unicomFHIRServlet;

    @MockBean
    JsonDataImporter jsonDataImporter;

//    @MockBean
//    private MedicinalProductElasticsearchRepository medicinalProductElasticsearchRepository;

    @Test
    public void medicinalProductSave() {
        MedicinalProduct medicinalProduct = new MedicinalProduct();

        PharmaceuticalProduct pharmaceuticalProduct = new PharmaceuticalProduct();
        pharmaceuticalProductRepository.save(pharmaceuticalProduct);

        medicinalProduct.setPharmaceuticalProduct(pharmaceuticalProduct);
        medicinalProductRepository.save(medicinalProduct);
    }
}