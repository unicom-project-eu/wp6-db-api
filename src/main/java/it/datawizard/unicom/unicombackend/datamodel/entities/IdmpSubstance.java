package it.datawizard.unicom.unicombackend.datamodel.entities;

import javax.persistence.*;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
public class IdmpSubstance {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "ingredients")
    private Set<IdmpMedicinalProduct> products;
}