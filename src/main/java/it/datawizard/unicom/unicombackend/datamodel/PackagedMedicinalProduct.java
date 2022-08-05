package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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

