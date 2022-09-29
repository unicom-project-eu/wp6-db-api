package it.datawizard.unicom.unicombackend.jpa.entity;

import it.datawizard.unicom.unicombackend.jpa.enums.EdqmRouteOfAdministration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class RouteOfAdministration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EdqmRouteOfAdministration routeOfAdministration;

    @ManyToOne
    @JoinColumn(name = "pharmaceuticalProduct_id")
    @ToString.Exclude
    private PharmaceuticalProduct pharmaceuticalProduct;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RouteOfAdministration that = (RouteOfAdministration) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
