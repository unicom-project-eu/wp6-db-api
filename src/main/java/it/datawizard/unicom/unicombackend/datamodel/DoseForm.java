package it.datawizard.unicom.unicombackend.datamodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class DoseForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String display;

    @OneToMany
    private Set<PharmaceuticalProduct> pharmaceuticalProducts;
}
