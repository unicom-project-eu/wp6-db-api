package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.deprecated.SubstanceWithRolePai;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceWithRolePaiRepository;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SubstanceResourceProvider implements IResourceProvider {
    private final static Logger LOG = LoggerFactory.getLogger(SubstanceResourceProvider.class);

    final private SubstanceWithRolePaiRepository substanceWithRolePaiRepository;

    @Autowired
    public SubstanceResourceProvider(SubstanceWithRolePaiRepository substanceWithRolePaiRepository) {
        this.substanceWithRolePaiRepository = substanceWithRolePaiRepository;
    }

    @Override
    public Class<Substance> getResourceType() {
        return Substance.class;
    }

    @Search
    public List<Substance> findAllResources() {
        ArrayList<Substance> substances = new ArrayList<>();

        for (SubstanceWithRolePai substanceWithRolePai: substanceWithRolePaiRepository.findAll()) {
            substances.add(substanceFromEntity(substanceWithRolePai));
        }

        return substances;
    }

    @Search
    public Substance findByIngredientCode(@RequiredParam(name = Substance.SP_CODE) StringParam code) {
        SubstanceWithRolePai substanceWithRolePai = substanceWithRolePaiRepository.findByIngredientCode(code.getValue());

        if (substanceWithRolePai == null)
            return null;

        return substanceFromEntity(substanceWithRolePai);
    }

    @Read
    public Substance getResourceById(@IdParam IdType id) {
        Optional<SubstanceWithRolePai> result = substanceWithRolePaiRepository.findById(id.getIdPartAsLong());
        return result.map(SubstanceResourceProvider::substanceFromEntity).orElse(null);
    }

    public static Substance substanceFromEntity(SubstanceWithRolePai substanceWithRolePai) {
        Substance substance = new Substance();

        // id
        substance.setId(substanceWithRolePai.getId().toString());

        CodeableReference codeableReference = new CodeableReference();
        CodeableConcept substanceCodeableConcept = new CodeableConcept();
        substanceCodeableConcept.addCoding(
                "https://spor.ema.europa.eu/v2/SubstanceDefinition",
                substanceWithRolePai.getIngredientCode(),
                substanceWithRolePai.getSubstanceName()
        );
        codeableReference.setConcept(substanceCodeableConcept);
        substance.setCode(codeableReference);

        return substance;
    }
}
