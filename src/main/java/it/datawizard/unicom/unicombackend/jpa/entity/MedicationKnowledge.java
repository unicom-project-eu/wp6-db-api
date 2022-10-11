package it.datawizard.unicom.unicombackend.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MedicationKnowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String status;
    private String author;
    private String intendedJurisdiction;
    private String name;
    private String relatedMedicationKnowledge;
    private String associatedMedication;
    private String productType;
    private String monograph;
    private String preparationInstruction;
    private Float cost;
    private String monitoringProgram;
    private String indicationGuideline;
    private String medicineClassification;
    private String packaging;
    private String clinicalUseIssue;
    private String storageGuideline;
    private String regulatory;
    private String definitional;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MedicationKnowledge medicationKnowledge = (MedicationKnowledge) o;
        return id != null && Objects.equals(id, medicationKnowledge.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}