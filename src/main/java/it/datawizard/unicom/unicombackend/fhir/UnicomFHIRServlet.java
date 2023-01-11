package it.datawizard.unicom.unicombackend.fhir;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import it.datawizard.unicom.unicombackend.fhir.plainproviders.ConformanceResourcesPlainProvider;
import it.datawizard.unicom.unicombackend.fhir.resourceproviders.*;
import it.datawizard.unicom.unicombackend.fhir.tenants.UnicomTenantIdentificationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnicomFHIRServlet extends RestfulServer {
    @Serial
    private static final long serialVersionUID = 1L;

    final private FhirContext fhirContext;
    final private UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy;
    final private MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider;
    final private IngredientResourceProvider ingredientResourceProvider;
    final private SubstanceDefinitionResourceProvider substanceDefinitionResourceProvider;
    final private SubstanceResourceProvider substanceResourceProvider;
    final private AdministrableProductDefinitionResourceProvider administrableProductDefinitionResourceProvider;
    final private MedicinalProductDefinitionResourceProvider medicinalProductDefinitionResourceProvider;
    final private PackagedProductDefinitionResourceProvider packagedProductDefinitionResourceProvider;
    final private ManufacturedItemDefinitionResourceProvider manufacturedItemDefinitionResourceProvider;
    final private ConformanceResourcesPlainProvider conformanceResourcesPlainProvider;

    final private OpenApiInterceptor unicomOpenApiInterceptor;

    @Autowired
    public UnicomFHIRServlet(MedicationKnowledgeResourceProvider medicationKnowledgeResourceProvider, OpenApiInterceptor openApiInterceptor, UnicomTenantIdentificationStrategy unicomTenantIdentificationStrategy, FhirContext fhirContext, IngredientResourceProvider ingredientResourceProvider, SubstanceDefinitionResourceProvider substanceDefinitionResourceProvider, SubstanceResourceProvider substanceResourceProvider, AdministrableProductDefinitionResourceProvider administrableProductDefinitionResourceProvider, MedicinalProductDefinitionResourceProvider medicinalProductDefinitionResourceProvider, PackagedProductDefinitionResourceProvider packagedProductDefinitionResourceProvider, ManufacturedItemDefinitionResourceProvider manufacturedItemDefinitionResourceProvider, ConformanceResourcesPlainProvider conformanceResourcesPlainProvider) {
        this.medicationKnowledgeResourceProvider = medicationKnowledgeResourceProvider;
        this.unicomOpenApiInterceptor = openApiInterceptor;
        this.unicomTenantIdentificationStrategy = unicomTenantIdentificationStrategy;
        this.fhirContext = fhirContext;
        this.ingredientResourceProvider = ingredientResourceProvider;
        this.substanceDefinitionResourceProvider = substanceDefinitionResourceProvider;
        this.substanceResourceProvider = substanceResourceProvider;
        this.administrableProductDefinitionResourceProvider = administrableProductDefinitionResourceProvider;
        this.medicinalProductDefinitionResourceProvider = medicinalProductDefinitionResourceProvider;
        this.packagedProductDefinitionResourceProvider = packagedProductDefinitionResourceProvider;
        this.manufacturedItemDefinitionResourceProvider = manufacturedItemDefinitionResourceProvider;
        this.conformanceResourcesPlainProvider = conformanceResourcesPlainProvider;
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
        List<IResourceProvider> resourceProviders = new ArrayList<>();
        resourceProviders.add(medicationKnowledgeResourceProvider);
        resourceProviders.add(ingredientResourceProvider);
        resourceProviders.add(substanceDefinitionResourceProvider);
        resourceProviders.add(substanceResourceProvider);
        resourceProviders.add(administrableProductDefinitionResourceProvider);
        resourceProviders.add(medicinalProductDefinitionResourceProvider);
        resourceProviders.add(packagedProductDefinitionResourceProvider);
        resourceProviders.add(manufacturedItemDefinitionResourceProvider);
        setResourceProviders(resourceProviders);

        // register plain providers
        registerProviders(conformanceResourcesPlainProvider);

        // register swagger interceptor
        registerInterceptor(unicomOpenApiInterceptor);

        //Set paging provider
        FifoMemoryPagingProvider fifoMemoryPagingProvider = new FifoMemoryPagingProvider(100);
        fifoMemoryPagingProvider.setDefaultPageSize(100);
        fifoMemoryPagingProvider.setMaximumPageSize(100);
        setPagingProvider(fifoMemoryPagingProvider);
    }
}
