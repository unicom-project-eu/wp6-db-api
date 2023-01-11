package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import it.datawizard.unicom.unicombackend.jpa.specification.IngredientSpecifications;
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
public class SubstanceResourceProvider implements IResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(SubstanceResourceProvider.class);
    private final IngredientRepository ingredientRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public SubstanceResourceProvider(IngredientRepository ingredientRepository, PlatformTransactionManager platformTransactionManager) {
        this.ingredientRepository = ingredientRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.transactionTemplate = new TransactionTemplate(this.platformTransactionManager);
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Substance.class;
    }

    @Read()
    @Transactional
    public Substance getResourceById(@IdParam IdType id) {
        return ingredientRepository.findById(id.getIdPartAsLong())
                .map(SubstanceResourceProvider::substanceFromEntity)
                .orElse(null);
    }

    @Search
    @Transactional
    public IBundleProvider findResources(
            RequestDetails requestDetails,
            @OptionalParam(name = Substance.SP_CATEGORY) StringParam category,
            @OptionalParam(name = Substance.SP_CODE) StringParam code,
            @OptionalParam(name = Substance.SP_CONTAINER_IDENTIFIER) StringParam containerIdentifier,
            @OptionalParam(name = Substance.SP_EXPIRY) DateParam expiry,
            @OptionalParam(name = Substance.SP_IDENTIFIER) StringParam identifier,
            @OptionalParam(name = Substance.SP_QUANTITY)QuantityParam quantity,
            @OptionalParam(name = Substance.SP_STATUS) StringParam status,
            @OptionalParam(name = Substance.SP_SUBSTANCE_REFERENCE) ReferenceParam substanceReference) {

        final String tenantId = requestDetails.getTenantId();
        final InstantType searchTime = InstantType.withCurrentTime();

        handleReferenceParamsExceptions(substanceReference);

        Specification<it.datawizard.unicom.unicombackend.jpa.entity.Ingredient> specification = Specification
                .where(tenantId != null ? IngredientSpecifications.isCountryEqualTo(tenantId) : null)
                .and(code != null ? IngredientSpecifications.isSubstanceCodeEqualTo(code.getValue()) : null);
        return new IBundleProvider() {

            final boolean shouldReturnEmptyResult = Stream.of(category, expiry, identifier, quantity, status, substanceReference).filter(bp -> bp != null).count() > 0;

            @Override
            public Integer size() {
                return shouldReturnEmptyResult? 0 : (int)ingredientRepository.findAll(specification,PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                List<IBaseResource> results = new ArrayList<>();

                if (!shouldReturnEmptyResult) {
                    transactionTemplate.execute(status -> {
                        Page<Ingredient> allSubstances = ingredientRepository.findAll(specification, PageRequest.of(currentPageIndex,pageSize));
                        results.addAll(allSubstances.stream()
                                .map(SubstanceResourceProvider::substanceFromEntity).toList());
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

    public static Substance substanceFromEntity(Ingredient ingredient)  {
        Substance substanceResource = new Substance();

        //Idx
        substanceResource.setId(ingredient.getId().toString());

        //Instance //TODO: setInstance
//        substanceResource.setInstance(true);

        //Code
        CodeableConcept substanceCodeCodeableConcept = new CodeableConcept();
        substanceCodeCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v2/SubstanceDefinition",//TODO: check link
                ingredient.getSubstance().getSubstanceCode(),
                ingredient.getSubstance().getSubstanceName()
        );
        substanceResource.setCode(substanceCodeCodeableConcept);

        return substanceResource;
    }

    private void handleReferenceParamsExceptions(ReferenceParam substanceReference) {
//        if (codeReference != null && codeReference.hasResourceType()) {
//            String codeReferenceResourceType = codeReference.getResourceType();
//            if (!"SubstanceDefinition".equals(codeReferenceResourceType)) {
//                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'codeReference': " + codeReferenceResourceType);
//            }
//        }

        if (substanceReference != null && substanceReference.hasResourceType()) {
            String substanceReferenceResourceType = substanceReference.getResourceType();
            if (!"Substance".equals(substanceReferenceResourceType)) {
                throw new InvalidRequestException(Msg.code(633) + "Invalid resource type for parameter 'substance': " + substanceReferenceResourceType);
            }
        }
    }
}
