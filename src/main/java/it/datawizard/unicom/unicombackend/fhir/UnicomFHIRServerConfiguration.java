package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantDescriptor;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
public class UnicomFHIRServerConfiguration {
    @Bean
    FhirContext getFhirContext() {
        FhirContext context = FhirContext.forR5();
        return context;
    }

    @Bean
    public LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors() {
        LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors = new LinkedHashMap<>();

        tenantDescriptors.put(TenantEnum.global, new TenantDescriptor("Global","glob"));
        tenantDescriptors.put(TenantEnum.belgium, new TenantDescriptor("Belgium","bel"));

        return tenantDescriptors;
    }
}
