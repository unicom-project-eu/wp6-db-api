package it.datawizard.unicom.unicombackend.elasticsearch.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class MedicinalProductRepositoryTest {
    public MedicinalProductRepository medicinalProductRepository;

    @Autowired
    public MedicinalProductRepositoryTest(MedicinalProductRepository medicinalProductRepository) {
        this.medicinalProductRepository = medicinalProductRepository;
    }

    @Test
    public void test() {
        MedicinalProduct medicinalProduct = new MedicinalProduct();
        medicinalProduct.setFullName("Aspirine blablabla");
        medicinalProduct.setPharmaceuticalProduct(new PharmaceuticalProduct());
        medicinalProductRepository.save(medicinalProduct);


        System.out.println(medicinalProductRepository.findFuzzyByFullName("Aspirine", Pageable.ofSize(1)));
    }

}