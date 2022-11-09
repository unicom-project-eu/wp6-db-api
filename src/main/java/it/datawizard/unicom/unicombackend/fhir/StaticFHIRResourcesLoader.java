package it.datawizard.unicom.unicombackend.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class StaticFHIRResourcesLoader {
    private static Logger LOG = LoggerFactory.getLogger(StaticFHIRResourcesLoader.class);
    private final HashMap<String, ArrayList<IBaseResource>>
        fhirResourcesByType = new HashMap<>();

    @Autowired
    public StaticFHIRResourcesLoader(ResourceLoader resourceLoader, FhirContext fhirContext) throws IOException {
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader);

        ArrayList<Resource> resourcesFileList = new ArrayList<>(Arrays.asList(
                resourcePatternResolver.getResources("classpath:fhir/**/*.json")));

        LOG.info("Found {} static resources", resourcesFileList.size());

        resourcesFileList.forEach(resource -> {
            try {
                IParser parser = fhirContext.newJsonParser();
                loadFhirResource(resource, parser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadFhirResource(Resource resourceFile, IParser parser) throws IOException {
        try (Reader reader = new InputStreamReader(resourceFile.getInputStream())) {
            IBaseResource baseResource = parser.parseResource(reader);

            if (!fhirResourcesByType.containsKey(baseResource.fhirType())) {
                fhirResourcesByType.put(baseResource.fhirType(), new ArrayList<>());
            }

            fhirResourcesByType.get(baseResource.fhirType()).add(baseResource);
        }
    }

    public ArrayList<IBaseResource> getFhirResourcesByType(String fhirType) {
        return fhirResourcesByType.getOrDefault(fhirType, new ArrayList<>());
    }
}
