package it.datawizard.unicom.unicombackend.fhir.tenants;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.tenant.UrlBaseTenantIdentificationStrategy;
import ca.uhn.fhir.util.UrlPathTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class UnicomTenantIdentificationStrategy extends UrlBaseTenantIdentificationStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(UnicomTenantIdentificationStrategy.class);
    @Override
    public void extractTenant(UrlPathTokenizer theUrlPathTokenizer, RequestDetails theRequestDetails) {
        super.extractTenant(theUrlPathTokenizer, theRequestDetails);

        LOG.trace("Tenant: " + theRequestDetails.getTenantId());
    }
}
