package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.MedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.edqm.EdqmRouteOfAdministration;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.MedicinalProductSpecifications;
import it.datawizard.unicom.unicombackend.jpa.specification.PharmaceuticalProductSpecifications;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdministrableProductDefinitionResourceProvider implements IResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(AdministrableProductDefinitionResourceProvider.class);
    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;


    @Override
    public Class<AdministrableProductDefinition> getResourceType() {
        return AdministrableProductDefinition.class;
    }

    @Autowired
    public AdministrableProductDefinitionResourceProvider(PharmaceuticalProductRepository pharmaceuticalProductRepository, PlatformTransactionManager platformTransactionManager) {
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Read()
    @Transactional
    public AdministrableProductDefinition getResourceById(@IdParam IdType id) {
        return administrableProductDefinitionFromEntity(
                pharmaceuticalProductRepository.findById(id.getIdPartAsLong()).orElse(null)
        );
    }



    @Search
    @Transactional
    public IBundleProvider findResources(RequestDetails requestDetails, @OptionalParam(name = AdministrableProductDefinition.SP_DOSE_FORM) StringParam administrableDoseForm, @OptionalParam(name = AdministrableProductDefinition.SP_ROUTE) StringParam routeOfAdministration) {
        //final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        Specification<PharmaceuticalProduct> specification = Specification
                .where(administrableDoseForm != null ? PharmaceuticalProductSpecifications.isAdministrableDoseFormEqualTo(administrableDoseForm.getValue()) : null)
                .and(routeOfAdministration != null ? PharmaceuticalProductSpecifications.routesOfAdministrationContains(routeOfAdministration.getValue()): null);

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)pharmaceuticalProductRepository.findAll(specification,PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                final List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<PharmaceuticalProduct> allMedicinalProducts = pharmaceuticalProductRepository.findAll(specification, PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allMedicinalProducts.stream()
                            .map(AdministrableProductDefinitionResourceProvider::administrableProductDefinitionFromEntity)
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

    public static AdministrableProductDefinition administrableProductDefinitionFromEntity(PharmaceuticalProduct pharmaceuticalProductEntity)  {
        AdministrableProductDefinition administrableProductDefinition = new AdministrableProductDefinition();

        // id
        administrableProductDefinition.setId(pharmaceuticalProductEntity.getId().toString());

        // Identifier
        ArrayList<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setSystem("http://ema.europa.eu/fhir/phpId");
        identifier.setValue(pharmaceuticalProductEntity.getIdmpPhpId());
        identifiers.add(identifier);
        administrableProductDefinition.setIdentifier(identifiers);

        // administrableDoseForm
        CodeableConcept administrableDoseFormCcodeableConcept = new CodeableConcept();
        administrableDoseFormCcodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000004",
                pharmaceuticalProductEntity.getAdministrableDoseForm().getCode(),
                pharmaceuticalProductEntity.getAdministrableDoseForm().getTerm()
        );
        administrableProductDefinition.setAdministrableDoseForm(administrableDoseFormCcodeableConcept);

        // unitOfPresentation
        CodeableConcept unitOfPresentationCodeableConcept = new CodeableConcept();
        unitOfPresentationCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v1/lists/200000000014",
                pharmaceuticalProductEntity.getUnitOfPresentation().getCode(),
                pharmaceuticalProductEntity.getUnitOfPresentation().getTerm()
        );
        administrableProductDefinition.setUnitOfPresentation(unitOfPresentationCodeableConcept);

        // rotesOfAdministration
        for (EdqmRouteOfAdministration edqmRouteOfAdministration : pharmaceuticalProductEntity.getRoutesOfAdministration()) {
            CodeableConcept routeOfAdministrationCodeableConcept = new CodeableConcept();
            routeOfAdministrationCodeableConcept.addCoding(
                    "https://spor.ema.europa.eu/v1/lists/100000073345",
                    edqmRouteOfAdministration.getCode(),
                    edqmRouteOfAdministration.getTerm()
            );

            AdministrableProductDefinition.AdministrableProductDefinitionRouteOfAdministrationComponent
                    fhirRouteOfAdministration = new AdministrableProductDefinition
                        .AdministrableProductDefinitionRouteOfAdministrationComponent(routeOfAdministrationCodeableConcept);

            administrableProductDefinition.addRouteOfAdministration(fhirRouteOfAdministration);
        }

        return administrableProductDefinition;
    }
}
