package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.IdmpDoseForm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class IdmpMedicinalProduct {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private IdmpDoseForm doseForm;
}
