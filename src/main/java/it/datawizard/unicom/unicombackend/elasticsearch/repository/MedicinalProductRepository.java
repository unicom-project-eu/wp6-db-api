package it.datawizard.unicom.unicombackend.elasticsearch.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MedicinalProductRepository extends ElasticsearchRepository<MedicinalProduct,Long> {
    Page<MedicinalProduct> findFuzzyByFullName(String name, Pageable pageable);
}
