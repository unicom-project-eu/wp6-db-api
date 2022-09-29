package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class EdqmConcept {
    @Id
    private String code;

    @Column(nullable = false)
    private String term;
}
