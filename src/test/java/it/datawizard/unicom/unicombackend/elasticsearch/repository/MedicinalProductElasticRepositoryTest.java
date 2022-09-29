package it.datawizard.unicom.unicombackend.elasticsearch.repository;

import it.datawizard.unicom.unicombackend.fhir.UnicomFHIRServlet;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    }
}
