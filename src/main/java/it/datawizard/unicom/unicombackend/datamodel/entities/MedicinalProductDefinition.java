package it.datawizard.unicom.unicombackend.datamodel.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class MedicinalProductDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String mpId;

    @Column(unique = true)
    private String pcId;

    private String description;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Organization marketingAuthorizationOlder;

    @OneToMany(mappedBy = "medicinalProductDefinition")
    private Set<Contains> contains;
}
