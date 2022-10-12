package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstanceRepository extends JpaRepository<Substance, String> {
}
