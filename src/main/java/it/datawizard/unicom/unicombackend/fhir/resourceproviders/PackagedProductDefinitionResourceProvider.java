package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmPackageItemType;
import it.datawizard.unicom.unicombackend.jpa.repository.PackagedMedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PackagedProductDefinitionResourceProvider implements IResourceProvider {
    private final PackagedMedicinalProductRepository packagedMedicinalProductRepository;

    @Autowired
    public PackagedProductDefinitionResourceProvider(PackagedMedicinalProductRepository packagedMedicinalProductRepository) {
        this.packagedMedicinalProductRepository = packagedMedicinalProductRepository;
    }


    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return PackagedProductDefinition.class;
    }

    @Read()
    @Transactional
    public PackagedProductDefinition getResourceById(@IdParam IdType id) {
        Optional<PackagedMedicinalProduct> result = packagedMedicinalProductRepository.findById(id.getIdPartAsLong());
        return result.map(PackagedProductDefinitionResourceProvider::packagedProductDefinitionFromEntity).orElse(null);
    }

    @Search
    @Transactional
    public List<PackagedProductDefinition> findAllResources() {
        ArrayList<PackagedProductDefinition> resources = new ArrayList<>();
        for (PackagedMedicinalProduct packagedMedicinalProduct: packagedMedicinalProductRepository.findAll()) {
            resources.add(packagedProductDefinitionFromEntity(packagedMedicinalProduct));
        }
        return resources;
    }

    @Search
    @Transactional
    public PackagedProductDefinition findByIdentifier(@RequiredParam(name = PackagedProductDefinition.SP_IDENTIFIER) StringParam identifier) {
        PackagedMedicinalProduct packagedMedicinalProduct = packagedMedicinalProductRepository.findByPcId(identifier.getValue());

        if (packagedMedicinalProduct == null)
            return null;

        return packagedProductDefinitionFromEntity(packagedMedicinalProduct);
    }

    public static PackagedProductDefinition packagedProductDefinitionFromEntity(PackagedMedicinalProduct packagedProductEntity) {
        PackagedProductDefinition packagedProductDefinition = new PackagedProductDefinition();
        packagedProductDefinition.setId(packagedProductEntity.getId().toString());

        //Identifier
        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/pcId"); //TODO: Check if this is the right system
        identifier.setValue(packagedProductEntity.getPcId());
        identifiers.add(identifier);
        packagedProductDefinition.setIdentifier(identifiers);

        //Country
        // TODO download all possible values and put them inside an entity
        //      * http://spor.ema.europa.eu/rmswi/#/lists/100000000002/terms
        CodeableConcept countryCodeableConcept = new CodeableConcept();
        countryCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000000002",
                "TODO use appropriate code",
                "TODO use appropriate term"
        );

        //Package
        Set<PackageItem> packageItemEntities = packagedProductEntity.getPackageItems();
        if (packageItemEntities.size() == 1) {
            packagedProductDefinition.setPackage(
                    packageFromEntity((PackageItem) packageItemEntities.toArray()[0])
            );
        }

        else if (packageItemEntities.size() > 1) {
            PackagedProductDefinition.PackagedProductDefinitionPackageComponent outerPackage
                    = new PackagedProductDefinition.PackagedProductDefinitionPackageComponent();

            outerPackage.setPackage(
                    packageItemEntities.stream().map(PackagedProductDefinitionResourceProvider::packageFromEntity)
                            .toList()
            );

            packagedProductDefinition.setPackage(outerPackage);
        }

        return packagedProductDefinition;
    }

    public static PackagedProductDefinition.PackagedProductDefinitionPackageComponent packageFromEntity(PackageItem packageItemEntity) {
        PackagedProductDefinition.PackagedProductDefinitionPackageComponent fhirPackage
                = new PackagedProductDefinition.PackagedProductDefinitionPackageComponent();

        //Type
        EdqmPackageItemType packageItemType = packageItemEntity.getType();
        if (packageItemType != null) {
            CodeableConcept packageTypeCodeableConcept = new CodeableConcept();
            packageTypeCodeableConcept.addCoding(
                    "https://spor.ema.europa.eu/v1/lists/100000073346",
                    packageItemEntity.getType().getCode(),
                    packageItemEntity.getType().getTerm()
            );
            fhirPackage.setType(packageTypeCodeableConcept);
        }

        // Quantity
        fhirPackage.setQuantity(packageItemEntity.getPackageItemQuantity());

        // Inner packages
        fhirPackage.setPackage(
                packageItemEntity.getChildrenPackageItems().stream()
                        .map(PackagedProductDefinitionResourceProvider::packageFromEntity)
                        .toList()
        );

        // Contained items (ManufacturedItems)
        fhirPackage.setContainedItem(
                packageItemEntity.getManufacturedItems().stream()
                        .map(PackagedProductDefinitionResourceProvider::containedItemFromEntity)
                        .toList()
        );

        return fhirPackage;
        //packageItemEntity.
//        PackagedProductDefinition.PackagedProductDefinitionPackageComponent packageContentComponent = new PackagedProductDefinition.PackagedProductDefinitionPackageComponent();
//        for (PackageItem packageItemEntity : packagedProductEntity.getPackageItems()) {
//            //Type
//            CodeableConcept packageTypeCodeableConcept = new CodeableConcept();
//            packageTypeCodeableConcept.addCoding(
//                    "https://spor.ema.europa.eu/v1/lists/100000073346",
//                    packageItemEntity.getType().getCode(),
//                    packageItemEntity.getType().getDefinition()
//            );
//            packageContentComponent.setType(packageTypeCodeableConcept);
//
//            //TODO: Set manufactured item references
//
//            packageContentComponent.setContainedItem(packageItemEntity.getManufacturedItems().stream().map((manufacturedItem) -> {
//                CodeableReference childPackageReference = new CodeableReference();
//                CodeableConcept childPackageCodeableConcept = new CodeableConcept();
//                //TODO: Find the right way to insert manufacturedProduct, as it should be provided as BackboneElement
//                childPackageCodeableConcept.addCoding(
//                        "TODO find the right coding if it exists", manufacturedItem.getId().toString(), manufacturedItem.getId().toString()
//                );
//                childPackageReference.setConcept(childPackageCodeableConcept);
//                return new PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent(childPackageReference);
//            }).collect(Collectors.toList()));
//
//            //Quantity
//            packageContentComponent.setQuantity(packageItemEntity.getPackageItemQuantity());
//            outerPackage.addPackage(packageContentComponent);
//        }
    }

    public static PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent containedItemFromEntity(ManufacturedItem manufacturedItem) {
        PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent fhirContainedItem =
                new PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent();

        // TODO finish implementing this

        return fhirContainedItem;
    }
}
