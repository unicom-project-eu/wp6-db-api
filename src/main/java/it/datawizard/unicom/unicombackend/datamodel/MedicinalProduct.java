package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Organization marketingAuthorizationHolder;

    @OneToMany(mappedBy = "medicinalProduct")
    private Set<PackagedMedicinalProduct> packagedMedicinalProducts;

    @ManyToMany
    Set<RouteOfAdministration> routesOfAdministration;
}
