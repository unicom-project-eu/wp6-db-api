package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class PackagedMedicinalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String pcId;
    private String atcCode;
    private Integer packageSize;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MedicinalProduct medicinalProduct;
}

