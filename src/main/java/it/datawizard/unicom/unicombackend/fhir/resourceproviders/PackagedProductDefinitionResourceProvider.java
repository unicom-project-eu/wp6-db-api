package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.ManufacturedItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackageItem;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmPackageItemType;
import it.datawizard.unicom.unicombackend.jpa.repository.PackagedMedicinalProductRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PackagedProductDefinitionResourceProvider implements IResourceProvider {
    private final PackagedMedicinalProductRepository packagedMedicinalProductRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public PackagedProductDefinitionResourceProvider(PackagedMedicinalProductRepository packagedMedicinalProductRepository, PlatformTransactionManager platformTransactionManager) {
        this.packagedMedicinalProductRepository = packagedMedicinalProductRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
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
        countryCodeableConcept.addCoding("https://spor.ema.europa.eu/v1/lists/100000000002", "TODO use appropriate code", "TODO use appropriate term");

        //Package
        Set<PackageItem> packageItemEntities = packagedProductEntity.getPackageItems();
        if (packageItemEntities.size() == 1) {
            packagedProductDefinition.setPackage(packageFromEntity((PackageItem) packageItemEntities.toArray()[0]));
        } else if (packageItemEntities.size() > 1) {
            PackagedProductDefinition.PackagedProductDefinitionPackageComponent outerPackage = new PackagedProductDefinition.PackagedProductDefinitionPackageComponent();

            outerPackage.setPackage(packageItemEntities.stream().map(PackagedProductDefinitionResourceProvider::packageFromEntity).toList());

            packagedProductDefinition.setPackage(outerPackage);
        }

        return packagedProductDefinition;
    }

    public static PackagedProductDefinition.PackagedProductDefinitionPackageComponent packageFromEntity(PackageItem packageItemEntity) {
        PackagedProductDefinition.PackagedProductDefinitionPackageComponent fhirPackage = new PackagedProductDefinition.PackagedProductDefinitionPackageComponent();

        //Type
        EdqmPackageItemType packageItemType = packageItemEntity.getType();
        if (packageItemType != null) {
            CodeableConcept packageTypeCodeableConcept = new CodeableConcept();
            packageTypeCodeableConcept.addCoding("https://spor.ema.europa.eu/v1/lists/100000073346", packageItemEntity.getType().getCode(), packageItemEntity.getType().getTerm());
            fhirPackage.setType(packageTypeCodeableConcept);
        }

        // Quantity
        Integer packageItemQuantity = packageItemEntity.getPackageItemQuantity();
        if (packageItemQuantity != null) {
            fhirPackage.setQuantity(packageItemQuantity);
        }

        // Inner packages
        fhirPackage.setPackage(packageItemEntity.getChildrenPackageItems().stream().map(PackagedProductDefinitionResourceProvider::packageFromEntity).toList());

        // Contained items (ManufacturedItems)
        fhirPackage.setContainedItem(packageItemEntity.getManufacturedItems().stream().map(PackagedProductDefinitionResourceProvider::containedItemFromEntity).toList());

        return fhirPackage;
    }

    public static PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent containedItemFromEntity(ManufacturedItem manufacturedItemEntity) {
        PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent fhirContainedItem = new PackagedProductDefinition.PackagedProductDefinitionPackageContainedItemComponent();

        // manufacturedItem
        CodeableReference manufacturedItemCodeableReference = new CodeableReference();
        Reference reference = new Reference();
        reference.setResource(ManufacturedItemDefinitionResourceProvider.manufacturedItemDefinitionFromEntity(manufacturedItemEntity));
        manufacturedItemCodeableReference.setReference(reference);
        fhirContainedItem.setItem(manufacturedItemCodeableReference);

        // amount
        Quantity quantity = new Quantity();
        quantity.setSystem("https://spor.ema.europa.eu/v1/lists/100000110633");
        quantity.setValue(manufacturedItemEntity.getManufacturedItemQuantity());
        quantity.setUnit(manufacturedItemEntity.getVolumeUnit());
        fhirContainedItem.setAmount(quantity);

        return fhirContainedItem;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return PackagedProductDefinition.class;
    }

    @Read()
    @Transactional
    public PackagedProductDefinition getResourceById(RequestDetails requestDetails, @NotNull @IdParam IdType id) {
        Optional<PackagedMedicinalProduct> result = packagedMedicinalProductRepository.findByIdAndMedicinalProduct_Country(id.getIdPartAsLong(), requestDetails.getTenantId());
        return result.map(PackagedProductDefinitionResourceProvider::packagedProductDefinitionFromEntity).orElse(null);
    }

    @Search
    @Transactional
    public IBundleProvider findAllResources(RequestDetails requestDetails) {
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int) packagedMedicinalProductRepository.findByMedicinalProduct_Country(requestDetails.getTenantId(), PageRequest.of(1, 1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex - theFromIndex;
                final int currentPageIndex = theFromIndex / pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<PackagedMedicinalProduct> allPackagedMedicinalProducts = packagedMedicinalProductRepository.findByMedicinalProduct_Country(requestDetails.getTenantId(), PageRequest.of(currentPageIndex, pageSize));
                    results.addAll(allPackagedMedicinalProducts.stream().map(PackagedProductDefinitionResourceProvider::packagedProductDefinitionFromEntity).toList());
                    return null;
                });

                return results;
            }

            @Override
            public InstantType getPublished() {
                return searchTime;
            }

            @Override
            public Integer preferredPageSize() {
                // Typically this method just returns null
                return null;
            }

            @Override
            public String getUuid() {
                return null;
            }
        };
    }

    @Search
    @Transactional
    public PackagedProductDefinition findByIdentifier(@RequiredParam(name = PackagedProductDefinition.SP_IDENTIFIER) StringParam identifier) {
        PackagedMedicinalProduct packagedMedicinalProduct = packagedMedicinalProductRepository.findByPcId(identifier.getValue());

        if (packagedMedicinalProduct == null) return null;

        return packagedProductDefinitionFromEntity(packagedMedicinalProduct);
    }
}
