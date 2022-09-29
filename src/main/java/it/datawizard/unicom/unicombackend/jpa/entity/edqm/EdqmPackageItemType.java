package it.datawizard.unicom.unicombackend.jpa.entity.edqm;

import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import it.datawizard.unicom.unicombackend.jpa.enums.IEdqmEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

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
