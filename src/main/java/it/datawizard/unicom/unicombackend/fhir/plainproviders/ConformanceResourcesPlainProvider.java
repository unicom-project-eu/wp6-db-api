package it.datawizard.unicom.unicombackend.fhir.plainproviders;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.BundleBuilder;
import it.datawizard.unicom.unicombackend.fhir.StaticFHIRResourcesLoader;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4b.model.*;
import org.springframework.stereotype.Component;

@Component
public class ConformanceResourcesPlainProvider {
    private final FhirContext fhirContext;
    private final StaticFHIRResourcesLoader staticFHIRResourcesLoader;

    public ConformanceResourcesPlainProvider(FhirContext fhirContext, StaticFHIRResourcesLoader staticFHIRResourcesLoader) {
        this.fhirContext = fhirContext;
        this.staticFHIRResourcesLoader = staticFHIRResourcesLoader;
    }

    @Search(type = StructureDefinition.class)
    public Bundle searchStructureDefinition() {
        return (Bundle) makeBundleBuilder("StructureDefinition").getBundle();
    }

    @Search(type = CodeSystem.class)
    public Bundle searchCodeSystem() {
        return (Bundle) makeBundleBuilder("CodeSystem").getBundle();
    }

    @Search(type = ValueSet.class)
    public Bundle searchValueSet() {
        return (Bundle) makeBundleBuilder("ValueSet").getBundle();
    }

    @Search(type = ImplementationGuide.class)
    public Bundle searchImplementationGuide() {
        return (Bundle) makeBundleBuilder("ImplementationGuide").getBundle();
    }


    private BundleBuilder makeBundleBuilder(String fhirType) {
        BundleBuilder bundleBuilder = new BundleBuilder(fhirContext);
        staticFHIRResourcesLoader.getFhirResourcesByType("CodeSystem")
                .forEach(resource -> {
                    IBase entry = bundleBuilder.addEntry();
                    bundleBuilder.addToEntry(entry, "resource", resource);
                });
        return bundleBuilder;
    }
}
