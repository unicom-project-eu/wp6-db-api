package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import it.datawizard.unicom.unicombackend.jackson.idresolver.EdqmDoseFormIdResolver;
import it.datawizard.unicom.unicombackend.jackson.idresolver.EdqmPackageItemTypeIdResolver;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@JsonIdentityInfo(property = "code", generator = ObjectIdGenerators.PropertyGenerator.class, resolver = EdqmPackageItemTypeIdResolver.class)
@JsonIdentityReference(alwaysAsId = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EdqmPackageItemType extends EdqmConcept {
    @OneToMany
    @ToString.Exclude
    private Set<PackageItem> packageItems;
}
