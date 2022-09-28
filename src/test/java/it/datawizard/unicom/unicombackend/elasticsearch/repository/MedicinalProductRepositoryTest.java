package it.datawizard.unicom.unicombackend.elasticsearch.repository;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataElasticsearchTest
public class MedicinalProductRepositoryTest {
    @Autowired
    private MedicinalProductRepository medicinalProductRepository;
    @MockBean
    private UnicomFHIRServlet unicomFHIRServlet;


    @Test
    public void test() {
        MedicinalProduct medicinalProduct = new MedicinalProduct();
        medicinalProduct.setFullName("Aspirine blablabla");
        medicinalProduct.setPharmaceuticalProduct(new PharmaceuticalProduct());

        medicinalProductRepository.save(medicinalProduct);
//        System.out.println(medicinalProductRepository.findFuzzyByFullName("Aspirine", Pageable.ofSize(1)));
    }
}
