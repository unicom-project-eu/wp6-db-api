package it.datawizard.unicom.unicombackend.jpa.entity;

import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmPackageItemType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PackageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
    private EdqmPackageItemType type;

    private Integer packageItemQuantity;

    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
    private PackagedMedicinalProduct packagedMedicinalProduct;


    @ManyToOne
    @JoinColumn()
    @ToString.Exclude
    private PackageItem parentPackageItem;

    @OneToMany(mappedBy = "parentPackageItem")
    private Set<PackageItem> childrenPackageItems;

    @OneToMany(mappedBy = "packageItem")
    private Set<ManufacturedItem> manufacturedItems;

}
