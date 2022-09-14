package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnicomFHIRServerConfiguration {
    @Bean
    FhirContext getFhirContext() {
        FhirContext context = FhirContext.forR5();
        return context;
    }
}
