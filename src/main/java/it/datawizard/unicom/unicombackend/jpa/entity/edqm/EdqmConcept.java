package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmConcept {
    @Id
    private String code;

    @Column(nullable = false)
    private String term;

    private String definition;
    private String comment;
}
