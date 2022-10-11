package it.datawizard.unicom.unicombackend.jpa.repository;

import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicationKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationKnowledgeRepository extends JpaRepository<MedicationKnowledge, Long> {

}
