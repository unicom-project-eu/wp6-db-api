package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.util.UrlPathTokenizer;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantDescriptor;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Configuration
public class UnicomTenantIdentificationStrategy extends UrlBaseTenantIdentificationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(UnicomTenantIdentificationStrategy.class);
    @Override
    public void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails) {
        super.extractTenant(theUrlPathTokenizer, theRequestDetails);

        LOG.trace("Tenant: " + theRequestDetails.getTenantId());
    }

    @Bean
    public LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors() {
        LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors = new LinkedHashMap<>();

        tenantDescriptors.put(TenantEnum.global, new TenantDescriptor("Global","glob"));
        tenantDescriptors.put(TenantEnum.belgium, new TenantDescriptor("Belgium","bel"));

        return tenantDescriptors;
    }
}
