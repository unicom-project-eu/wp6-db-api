package it.datawizard.unicom.unicombackend.fhir;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.AdministrableProductDefinitionResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.IngredientResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.MedicationKnowledgeResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.MedicinalProductDefinitionResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.tenants.UnicomTenantIdentificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.annotation.WebServlet;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/*", displayName = "fhir")
@Service
public class UnicomFHIRServlet extends RestfulServer {
    @Serial
    private static final long serialVersionUID = 1L;

    final private FhirContext fhirContext;
    final private UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy;
    final private MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider;
    final private IngredientResourceProvider ingredientResourceProvider;
    final private AdministrableProductDefinitionResourceProvider administrableProductDefinitionResourceProvider;
    final private MedicinalProductDefinitionResourceProvider medicinalProductDefinitionResourceProvider;

    final private UnicomOpenApiInterceptor unicomOpenApiInterceptor;

    @Autowired
    public UnicomFHIRServlet(MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider, UnicomOpenApiInterceptor unicomOpenApiInterceptor, UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy, FhirContext fhirContext, IngredientResourceProvider ingredientResourceProvider, AdministrableProductDefinitionResourceProvider administrableProductDefinitionResourceProvider, MedicinalProductDefinitionResourceProvider medicinalProductDefinitionResourceProvider) {
        this.medicationKnowledgeResourceProvider = medicationKnowledgeResourceProvider;
        this.unicomOpenApiInterceptor = unicomOpenApiInterceptor;
        this.unicomTenantIdentificationStrategy = unicomTenantIdentificationStrategy;
        this.fhirContext = fhirContext;
        this.ingredientResourceProvider = ingredientResourceProvider;
        this.administrableProductDefinitionResourceProvider = administrableProductDefinitionResourceProvider;
        this.medicinalProductDefinitionResourceProvider = medicinalProductDefinitionResourceProvider;
    }

    /**
     * The initialize method is automatically called when the servlet is starting up, so it can
     * be used to configure the servlet to define resource providers, or set up
     * configuration, interceptors, etc.
     */
    @Override
    protected void initialize() {
        // FHIR Version
        setFhirContext(fhirContext);

        // set tenant identification strategy
        setTenantIdentificationStrategy(unicomTenantIdentificationStrategy);

        // register resource providers
        List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
        resourceProviders.add(medicationKnowledgeResourceProvider);
        resourceProviders.add(ingredientResourceProvider);
        resourceProviders.add(administrableProductDefinitionResourceProvider);
        resourceProviders.add(medicinalProductDefinitionResourceProvider);
        setResourceProviders(resourceProviders);

        // register swagger interceptor
        registerInterceptor(unicomOpenApiInterceptor);
    }
}
