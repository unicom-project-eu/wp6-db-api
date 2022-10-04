package it.datawizard.unicom.unicombackend.fhir.resourceproviders;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import it.datawizard.unicom.unicombackend.jpa.entity.Ingredient;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r5.model.AdministrableProductDefinition;
import org.hl7.fhir.r5.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdministrableProductDefinitionResourceProvider implements IResourceProvider {
    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;

    @Override
    public Class<AdministrableProductDefinition> getResourceType() {
        return AdministrableProductDefinition.class;
    }

    @Autowired
    public AdministrableProductDefinitionResourceProvider(PharmaceuticalProductRepository pharmaceuticalProductRepository) {
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
    }

    @Read()
    public AdministrableProductDefinition getResourceById(@IdParam IdType id) {
        throw new NotImplementedException();
    }
}
