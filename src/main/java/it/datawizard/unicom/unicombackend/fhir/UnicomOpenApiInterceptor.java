package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantDescriptor;
import it.datawizard.unicom.unicombackend.fhir.tenants.TenantEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class UnicomOpenApiInterceptor extends OpenApiInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(UnicomOpenApiInterceptor.class);

    private LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors;

    @Autowired
    void setTenantDescriptors(LinkedHashMap<TenantEnum, TenantDescriptor> tenantDescriptors) {
        this.tenantDescriptors = tenantDescriptors;
    }

    @Override
    protected OpenAPI generateOpenApi(ServletRequestDetails theRequestDetails) {
        OpenAPI openAPI = super.generateOpenApi(theRequestDetails);

        String baseUrl = openAPI.getServers().get(0).getUrl();
        openAPI.getServers().clear();

        for (Map.Entry<TenantEnum, TenantDescriptor> entry: tenantDescriptors.entrySet()) {
            TenantDescriptor tenantDescriptor = entry.getValue();
            Server server = new Server();
            server.setUrl(baseUrl + "/" + tenantDescriptor.getSlug());
            server.setDescription(tenantDescriptor.getName());

            openAPI.getServers().add(server);
        }

        return openAPI;
    }
}
