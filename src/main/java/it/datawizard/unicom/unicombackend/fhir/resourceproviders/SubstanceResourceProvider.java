package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.SubstanceWithRolePai;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceWithRolePaiRepository;
import org.hl7.fhir.r4.model.Substance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubstanceResourceProvider implements IResourceProvider {
    private final static Logger LOG = LoggerFactory.getLogger(SubstanceResourceProvider.class);

    private SubstanceWithRolePaiRepository substanceWithRolePaiRepository;

    @Override
    public Class<Substance> getResourceType() {
        return Substance.class;
    }

    @Search
    public List<Substance> getAllResources() {
        ArrayList<Substance> substances = new ArrayList<>();

        for (SubstanceWithRolePai substanceWithRolePai: substanceWithRolePaiRepository.findAll()) {
            Substance substance = new Substance();

            substance.setDescription(substanceWithRolePai.getMoiety() + substanceWithRolePai.getModifier());

            substances.add(substance);
        }

        return substances;
    }

    @Autowired
    public void setSubstanceWithRolePaiRepository(SubstanceWithRolePaiRepository substanceWithRolePaiRepository) {
        this.substanceWithRolePaiRepository = substanceWithRolePaiRepository;
    }
}
