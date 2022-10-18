package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Substance;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceRepository;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SubstanceDefinitionResourceProvider implements IResourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(SubstanceDefinitionResourceProvider.class);
    private final SubstanceRepository substanceRepository;

    @Autowired
    public SubstanceDefinitionResourceProvider(SubstanceRepository substanceRepository) {
        this.substanceRepository = substanceRepository;
    }

    @Override
    public Class<SubstanceDefinition> getResourceType() {
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
    public List<SubstanceDefinition> findAllResources() {
        return substanceRepository.findAll().stream().map(
                SubstanceDefinitionResourceProvider::substanceDefinitionFromEntity
        ).toList();
    }

    public static SubstanceDefinition substanceDefinitionFromEntity(Substance substanceEntity)  {
        SubstanceDefinition substanceDefinition = new SubstanceDefinition();
        substanceDefinition.setId(substanceEntity.getSubstanceCode());

        return substanceDefinition;
    }
}
