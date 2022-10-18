package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceRepository;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubstanceDefinitionResourceProvider implements IResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(SubstanceDefinitionResourceProvider.class);
    private final SubstanceRepository substanceRepository;

    @Autowired
    public SubstanceDefinitionResourceProvider(SubstanceRepository substanceRepository) {
        this.substanceRepository = substanceRepository;
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

//    @Search
//    @Transactional
//    public List<SubstanceDefinition> findAllResources() {
//        return substanceRepository.findAll().stream().map(
//                SubstanceDefinitionResourceProvider::substanceDefinitionFromEntity
//        ).toList();
//    }

    @Search
    @Transactional
    public IBundleProvider findAllResources() {
        final InstantType searchTime = InstantType.withCurrentTime();
        final List<Long> allSubstanceCodes = substanceRepository.getAllSubstanceCodes();

        return new IBundleProvider() {

            @Override
            public Integer size() {
                return allSubstanceCodes.size();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                int end = Math.max(theToIndex, allSubstanceCodes.size() - 1);
                List<Long> idsToReturn = allSubstanceCodes.subList(theFromIndex, end);
                return loadResourcesByIds(idsToReturn);
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

    private List<IBaseResource> loadResourcesByIds(List<Long> idsList) {
        return substanceRepository.findAllById(idsList.stream().map(i -> i.toString()).collect(Collectors.toList())).stream()
                .map(SubstanceDefinitionResourceProvider::substanceDefinitionFromEntity).collect(Collectors.toList());
    }

    public static SubstanceDefinition substanceDefinitionFromEntity(Substance substanceEntity)  {
        SubstanceDefinition substanceDefinition = new SubstanceDefinition();
        substanceDefinition.setId(substanceEntity.getSubstanceCode());

        return substanceDefinition;
    }
}
