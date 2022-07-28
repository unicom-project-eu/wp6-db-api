package it.datawizard.unicom.unicombackend.datamodel.entities;

import it.datawizard.unicom.unicombackend.datamodel.util.JsonData;
import it.datawizard.unicom.unicombackend.datamodel.util.JsonDataConverter;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
public class LocalSubstanceData {
    public static class Data extends JsonData {}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Convert(converter = JsonDataConverter.class)
    private Data data;
}

