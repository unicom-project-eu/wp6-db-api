package it.datawizard.unicom.unicombackend.datamodel.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class MedicinalProductDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String mpId;

    @Column(unique = true)
    private String pcId;

    private String description;

    @OneToMany(mappedBy = "medicinalProductDefinition")
    Set<ProductContainsItem> contains;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMpId() { return mpId; }
    public void setMpId(String mpId) { this.mpId = mpId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
