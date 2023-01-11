package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.IngredientSpecifications;
import it.datawizard.unicom.unicombackend.jpa.specification.SubstanceSpecifications;
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
import java.util.stream.Stream;

@Component
public class SubstanceDefinitionResourceProvider implements IResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(SubstanceDefinitionResourceProvider.class);
    private final SubstanceRepository substanceRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public SubstanceDefinitionResourceProvider(SubstanceRepository substanceRepository, PlatformTransactionManager platformTransactionManager) {
        this.substanceRepository = substanceRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return SubstanceDefinition.class;
    }

    @Read()
    @Transactional
    public SubstanceDefinition getResourceById(@IdParam IdType id) {
        return substanceRepository.findById(id.getIdPart())
                .map(SubstanceDefinitionResourceProvider::substanceDefinitionFromEntity)
                .orElse(null);
    }

    @Search
    @Transactional
    public IBundleProvider findResources(
            @OptionalParam(name = SubstanceDefinition.SP_CLASSIFICATION) StringParam classification,
            @OptionalParam(name = SubstanceDefinition.SP_CODE) StringParam code,
            @OptionalParam(name = SubstanceDefinition.SP_DOMAIN) StringParam domain,
            @OptionalParam(name = SubstanceDefinition.SP_IDENTIFIER) StringParam identifier,
            @OptionalParam(name = SubstanceDefinition.SP_NAME) StringParam name) {

        final InstantType searchTime = InstantType.withCurrentTime();

        Specification<Substance> specification = Specification
                .where(code != null ? SubstanceSpecifications.isSubstanceCodeEqualTo(code.getValue()) : null)
                .and(name != null ? SubstanceSpecifications.isSubstanceNameEqualTo(name.getValue()) : null);

        return new IBundleProvider() {

            final boolean shouldReturnEmptyResult = Stream.of(classification, domain, identifier).filter(bp -> bp != null).count() > 0;

            @Override
            public Integer size() {
                return shouldReturnEmptyResult? 0 : (int)substanceRepository.findAll(specification, PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                List<IBaseResource> results = new ArrayList<>();

                if(!shouldReturnEmptyResult) {
                    transactionTemplate.execute(status -> {
                        Page<Substance> allSubstances = substanceRepository.findAll(specification, PageRequest.of(currentPageIndex,pageSize));
                        results.addAll(allSubstances.stream()
                                .map(SubstanceDefinitionResourceProvider::substanceDefinitionFromEntity).toList());
                        return null;
                    });
                }
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

    public static SubstanceDefinition substanceDefinitionFromEntity(Substance substanceEntity)  {
        SubstanceDefinition substanceDefinition = new SubstanceDefinition();

        //Id
        substanceDefinition.setId(substanceEntity.getSubstanceCode());

        //Code
        List<SubstanceDefinition.SubstanceDefinitionCodeComponent> substanceDefinitionCodeComponentList = new ArrayList<>();
        SubstanceDefinition.SubstanceDefinitionCodeComponent substanceDefinitionCodeComponent = new SubstanceDefinition.SubstanceDefinitionCodeComponent();
        CodeableConcept substanceCodeCodeableConcept = new CodeableConcept();
        substanceCodeCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v2/SubstanceDefinition",
                substanceEntity.getSubstanceCode(),
                substanceEntity.getSubstanceName()
        );
        substanceDefinitionCodeComponent.setCode(substanceCodeCodeableConcept);
        substanceDefinitionCodeComponentList.add(substanceDefinitionCodeComponent);
        substanceDefinition.setCode(substanceDefinitionCodeComponentList);

        //Name
        List<SubstanceDefinition.SubstanceDefinitionNameComponent> substanceDefinitionNameComponentList = new ArrayList<>();
        SubstanceDefinition.SubstanceDefinitionNameComponent substanceDefinitionNameComponent = new SubstanceDefinition.SubstanceDefinitionNameComponent();
        substanceDefinitionNameComponent.setName(substanceEntity.getSubstanceName());
        substanceDefinitionNameComponentList.add(substanceDefinitionNameComponent);
        substanceDefinition.setName(substanceDefinitionNameComponentList);

        return substanceDefinition;
    }
}
