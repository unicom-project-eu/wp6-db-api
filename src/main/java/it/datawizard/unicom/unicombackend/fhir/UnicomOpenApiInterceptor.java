package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import io.swagger.v3.oas.models.OpenAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnicomOpenApiInterceptor extends OpenApiInterceptor {
    private final static Logger LOG = LoggerFactory.getLogger(UnicomOpenApiInterceptor.class);

    @Override
    protected OpenAPI generateOpenApi(ServletRequestDetails theRequestDetails) {
        OpenAPI openAPI = super.generateOpenApi(theRequestDetails);
        return openAPI;
    }
}
