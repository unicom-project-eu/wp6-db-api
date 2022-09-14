package it.datawizard.unicom.unicombackend.fhir;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.MedicationKnowledgeResourceProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.SubstanceResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/*", displayName = "fhir")
@Service
@Configuration
public class UnicomFHIRServlet extends RestfulServer {
    @Serial
    private static final long serialVersionUID = 1L;

    final UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy;
    final private MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider;
    final private SubstanceResourceProvider substanceResourceProvider;
    final private UnicomOpenApiInterceptor unicomOpenApiInterceptor;

    @Autowired
    public UnicomFHIRServlet(MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider, SubstanceResourceProvider substanceResourceProvider, UnicomOpenApiInterceptor unicomOpenApiInterceptor, UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy) {
        this.medicationKnowledgeResourceProvider = medicationKnowledgeResourceProvider;
        this.substanceResourceProvider = substanceResourceProvider;
        this.unicomOpenApiInterceptor = unicomOpenApiInterceptor;
        this.unicomTenantIdentificationStrategy = unicomTenantIdentificationStrategy;
    }

    /**
     * The initialize method is automatically called when the servlet is starting up, so it can
     * be used to configure the servlet to define resource providers, or set up
     * configuration, interceptors, etc.
     */
    @Override
    protected void initialize() {
        // FHIR Version
        setFhirContext(FhirContext.forR5());

        // set tenant identification strategy
        setTenantIdentificationStrategy(unicomTenantIdentificationStrategy);

        // register resource providers
        List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
        resourceProviders.add(medicationKnowledgeResourceProvider);
        resourceProviders.add(substanceResourceProvider);
        setResourceProviders(resourceProviders);

        // register swagger interceptor
        registerInterceptor(unicomOpenApiInterceptor);
    }
}
