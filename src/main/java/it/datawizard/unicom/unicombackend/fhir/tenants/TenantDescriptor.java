package it.datawizard.unicom.unicombackend.fhir.tenants;

import lombok.Data;

@Data
public class TenantDescriptor {
    private final String name;
    private final String slug;
    private final String description = "";
}
