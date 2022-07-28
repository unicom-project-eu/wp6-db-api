package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.IdmpDoseForm;

import javax.persistence.*;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
public class IdmpMedicinalProduct {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private IdmpDoseForm doseForm;
    private Integer packageSize;

    @ManyToMany
    private Set<IdmpSubstance> ingredients;
}
