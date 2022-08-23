package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.SubstanceWithRolePai;
import it.datawizard.unicom.unicombackend.jpa.repository.SubstanceWithRolePaiRepository;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
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

    @Autowired
    SubstanceWithRolePaiRepository substanceWithRolePaiRepository;

    @Override
    public Class<Substance> getResourceType() {
        return Substance.class;
    }

    @Search
    public List<Substance> getAllResources() {
        ArrayList<Substance> substances = new ArrayList<>();

        for (SubstanceWithRolePai substanceWithRolePai: substanceWithRolePaiRepository.findAll()) {
            substances.add(substanceFromEntity(substanceWithRolePai));
        }

        return substances;
    }

    public static Substance substanceFromEntity(SubstanceWithRolePai substanceWithRolePai) {
        Substance substance = new Substance();

        // id
        substance.setId(substanceWithRolePai.getId().toString());

        // ingredientCode
        CodeableConcept ingredientCode = new CodeableConcept();
        ArrayList<Coding> codings = new ArrayList<>();
        Coding coding = new Coding();
        coding.setSystem("TO BE CHANGED / SMS");
        coding.setCode(substanceWithRolePai.getIngredientCode());
        codings.add(coding);
        ingredientCode.setCoding(codings);
        substance.setCode(ingredientCode);

        // description
        substance.setDescription(substanceWithRolePai.getMoiety() + (
                substanceWithRolePai.getModifier() != null
                ? " " + substanceWithRolePai.getModifier()
                : ""
            ));

        return substance;
    }
}
