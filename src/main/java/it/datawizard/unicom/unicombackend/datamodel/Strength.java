package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Strength {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    // TODO normalize units
    private Float numeratorValue;
    private String numeratorUnit;

    private Float denominatorValue;
    private String denominatorUnit;
}
