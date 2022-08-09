package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class UnitOfPresentation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    @OneToMany(mappedBy = "unitOfPresentation")
    private Set<MedicinalProduct> medicinalProducts;
}
