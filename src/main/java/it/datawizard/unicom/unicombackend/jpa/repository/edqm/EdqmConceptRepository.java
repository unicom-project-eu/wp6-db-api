package it.datawizard.unicom.unicombackend.jpa.repository.edqm;

import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmConcept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EdqmConceptRepository<T extends EdqmConcept> extends JpaRepository<T, String> {
}
