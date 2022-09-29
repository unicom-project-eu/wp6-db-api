package it.datawizard.unicom.unicombackend.elasticsearch.repository;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.AutoConfigureDataElasticsearch;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataElasticsearchTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Getter
@Setter
public class MedicinalProductElasticRepositoryTest {
    @Autowired
    private MedicinalProductElasticsearchRepository medicinalProductElasticsearchRepository;

    @MockBean
    private UnicomFHIRServlet unicomFHIRServlet;

    @MockBean
    private MedicinalProductRepository medicinalProductRepository;

    @Test
    public void testFuzzySearch() {
        // TODO: Find a way to test without affecting the indices
//
//        MedicinalProduct medicinalProduct = new MedicinalProduct();
//        medicinalProduct.setFullName("Wonderdrug");
//        medicinalProduct.setId(-1L);
//        medicinalProductElasticsearchRepository.save(medicinalProduct);
//
//        medicinalProduct = new MedicinalProduct();
//        medicinalProduct.setFullName("IwonDerDrug");
//        medicinalProduct.setId(-2L);
//        medicinalProductElasticsearchRepository.save(medicinalProduct);
//
//        medicinalProductElasticsearchRepository.findFuzzyByFullName("won", Pageable.ofSize(10));
    }
}
