package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.AtcCode;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.MedicinalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.MedicinalProductSpecifications;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.*;

@Component
public class MedicinalProductDefinitionResourceProvider implements IResourceProvider {
    private final MedicinalProductRepository medicinalProductRepository;
    final private PlatformTransactionManager platformTransactionManager;
    final private TransactionTemplate transactionTemplate;

    @Autowired
    public MedicinalProductDefinitionResourceProvider(MedicinalProductRepository medicinalProductRepository, PlatformTransactionManager platformTransactionManager) {
        this.medicinalProductRepository = medicinalProductRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }


    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return MedicinalProductDefinition.class;
    }

    @Read()
    @Transactional
    public MedicinalProductDefinition getResourceById(RequestDetails requestDetails, @IdParam IdType id) {
        Optional<MedicinalProduct> result = medicinalProductRepository.findByIdAndCountry(id.getIdPartAsLong(), requestDetails.getTenantId());
        return result.map(MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity).orElse(null);
    }

    @Search
    @Transactional
    public IBundleProvider findAllResources(RequestDetails requestDetails) {
        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)medicinalProductRepository.findByCountry(tenantId, PageRequest.of(1,1))
                        .getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<MedicinalProduct> allMedicinalProducts = medicinalProductRepository
                            .findByCountry(tenantId, PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allMedicinalProducts.stream()
                            .map(MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity)
                            .toList());
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
    public IBundleProvider findByCountryAndNameAndClassification(RequestDetails requestDetails,@OptionalParam(name = MedicinalProductDefinition.SP_NAME) StringParam name, @OptionalParam(name = MedicinalProductDefinition.SP_PRODUCT_CLASSIFICATION) StringParam classification) {
        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        Specification<MedicinalProduct> specification = Specification
                .where(tenantId != null ? MedicinalProductSpecifications.isCountryEqualTo(tenantId) : null)
                .and(name != null ? MedicinalProductSpecifications.isFullNameEqualTo(name.getValue()) : null)
                .and(classification != null ? MedicinalProductSpecifications.atcCodesContains(classification.getValue()): null);

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)medicinalProductRepository.findAll(specification,PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<MedicinalProduct> allMedicinalProducts = medicinalProductRepository.findAll(specification, PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allMedicinalProducts.stream()
                            .map(MedicinalProductDefinitionResourceProvider::medicinalProductDefinitionFromEntity)
                            .toList());
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


    public static MedicinalProductDefinition medicinalProductDefinitionFromEntity(MedicinalProduct medicinalProductEntity) {
        MedicinalProductDefinition medicinalProductDefinition = new MedicinalProductDefinition();
        medicinalProductDefinition.setId(medicinalProductEntity.getId().toString());

        //Identifier
        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/mpId");
        identifier.setValue(medicinalProductEntity.getMpId());
        identifiers.add(identifier);
        medicinalProductDefinition.setIdentifier(identifiers);

        //Type
        CodeableConcept typeCodeableConcept = new CodeableConcept();
        typeCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000000002",
                "200000025916",
                "Authorised Medicinal Product"
        );
        medicinalProductDefinition.setType(typeCodeableConcept);

        //Country
        // TODO download all possible values and put them inside an entity
        //      * http://spor.ema.europa.eu/rmswi/#/lists/100000000002/terms
        CodeableConcept countryCodeableConcept = new CodeableConcept();
        countryCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/100000000002",
                "TODO use appropriate code",
                "TODO use appropriate term"
        );

        //Name
        ArrayList<MedicinalProductDefinition.MedicinalProductDefinitionNameComponent> names = new ArrayList<>();
        MedicinalProductDefinition.MedicinalProductDefinitionNameComponent name = new MedicinalProductDefinition.MedicinalProductDefinitionNameComponent();
        name.setProductName(medicinalProductEntity.getFullName());
        names.add(name);
        medicinalProductDefinition.setName(names);

        //Classification
        ArrayList<CodeableConcept> codes = new ArrayList<>();
        for (AtcCode code : medicinalProductEntity.getAtcCodes()) {
            CodeableConcept codeCodeableConcept = new CodeableConcept();
            codeCodeableConcept.addCoding(
                    "https://spor.ema.europa.eu/v1/lists/100000093533",
                    code.getAtcCode(),
                    null
            );
            codes.add(codeCodeableConcept);
        }
        medicinalProductDefinition.setClassification(codes);

        // authorizedPharmaceuticalDoseForm
        CodeableConcept authorizedPharmaceuticalDoseFormCodeableConcept = new CodeableConcept();
        authorizedPharmaceuticalDoseFormCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000004",
                medicinalProductEntity.getAuthorizedPharmaceuticalDoseForm().getCode(),
                medicinalProductEntity.getAuthorizedPharmaceuticalDoseForm().getTerm()
        );

        // TODO
        medicinalProductDefinition.getExtension().add(new Extension(
           "TODO use appropriate url for extension",
                authorizedPharmaceuticalDoseFormCodeableConcept
        ));

        // TODO marketing authorization holder
        //      * https://build.fhir.org/medicinalproductdefinition.html#authorisations
        //        A key aspect of a regulated medicinal product is the authorisation (marketing authorisation).
        //        This is not directly carried on the product resource, but instead a RegulatedAuthorization resource is
        //        created which points back to this resource.

        return medicinalProductDefinition;
    }
}
