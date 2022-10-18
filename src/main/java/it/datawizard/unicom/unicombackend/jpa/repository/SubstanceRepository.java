package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubstanceRepository extends JpaRepository<Substance, String> {
    @Query("SELECT s.substanceCode from Substance s")
    List<Long> getAllSubstanceCodes();
}
