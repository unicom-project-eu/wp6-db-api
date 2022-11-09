package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.repository.IngredientRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public IBundleProvider findAllResources() {
        final InstantType searchTime = InstantType.withCurrentTime();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return (int)ingredientRepository.findAll(PageRequest.of(1,1)).getTotalElements();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                final int pageSize = theToIndex-theFromIndex;
                final int currentPageIndex = theFromIndex/pageSize;

                List<IBaseResource> results = new ArrayList<>();

                transactionTemplate.execute(status -> {
                    Page<Ingredient> allSubstances = ingredientRepository.findAll(PageRequest.of(currentPageIndex,pageSize));
                    results.addAll(allSubstances.stream()
                            .map(SubstanceResourceProvider::substanceFromEntity).toList());
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

    public static Substance substanceFromEntity(Ingredient ingredient)  {
        Substance substanceResource = new Substance();

        //Id
        substanceResource.setId(ingredient.getId().toString());

        //Instance
        substanceResource.setInstance(true);

        //Code
        CodeableReference substanceDefinitionCodeableReference = new CodeableReference();
        Reference reference = new Reference();
        reference.setResource(SubstanceDefinitionResourceProvider.substanceDefinitionFromEntity(ingredient.getSubstance()));
        substanceDefinitionCodeableReference.setReference(reference);
        substanceResource.setCode(substanceDefinitionCodeableReference);

        return substanceResource;
    }
}
