package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
public class UnitOfPresentation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    @OneToMany(mappedBy = "unitOfPresentation")
    private Set<MedicinalProduct> medicinalProducts;
}
