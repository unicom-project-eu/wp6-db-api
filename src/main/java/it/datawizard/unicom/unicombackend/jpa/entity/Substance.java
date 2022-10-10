package it.datawizard.unicom.unicombackend.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@JsonIdentityInfo(property = "substanceCode", generator = ObjectIdGenerators.PropertyGenerator.class)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Substance {
    @Id
    private String substanceCode;
    private String substanceName;

    private String moietyCode;
    private String moietyName;

    @OneToMany(mappedBy = "substance")
    @ToString.Exclude
    private Set<Ingredient> ingredients;
}
