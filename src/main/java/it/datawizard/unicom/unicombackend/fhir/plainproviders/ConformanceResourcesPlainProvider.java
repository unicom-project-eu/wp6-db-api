package it.datawizard.unicom.unicombackend.fhir.plainproviders;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.r5.model.*;
import org.springframework.stereotype.Component;

@Component
public class ConformanceResourcesPlainProvider {
    private final FhirContext fhirContext;

    public ConformanceResourcesPlainProvider(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Search(type = StructureDefinition.class)
    public Bundle searchStructureDefinition() {
        return (Bundle) new BundleBuilder(fhirContext)
                .getBundle();
    }

    @Search(type = CodeSystem.class)
    public Bundle searchCodeSystem() {
        return (Bundle) new BundleBuilder(fhirContext)
                .getBundle();
    }

    @Search(type = ValueSet.class)
    public Bundle searchValueSet() {
        return (Bundle) new BundleBuilder(fhirContext)
                .getBundle();
    }

    @Search(type = ImplementationGuide.class)
    public Bundle searchImplementationGuide() {
        return (Bundle) new BundleBuilder(fhirContext)
                .getBundle();
    }
}
