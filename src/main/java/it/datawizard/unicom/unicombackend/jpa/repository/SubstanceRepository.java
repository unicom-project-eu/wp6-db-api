package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SubstanceRepository extends JpaRepository<Substance, String>, JpaSpecificationExecutor<Substance> {
}
