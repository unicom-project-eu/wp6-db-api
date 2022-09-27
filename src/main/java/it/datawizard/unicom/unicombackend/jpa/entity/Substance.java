package it.datawizard.unicom.unicombackend.jpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Substance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private String name;
    private String moiety;

    @OneToMany(mappedBy = "substance")
    @ToString.Exclude
    private Set<Ingredient> ingredients;
}
