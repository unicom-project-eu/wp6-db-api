package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class MedicinalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mpId;
    private String fullName;
    private Integer packSize;
    private String packDescription;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PharmaceuticalProduct pharmaceuticalProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UnitOfPresentation unitOfPresentation;
}
