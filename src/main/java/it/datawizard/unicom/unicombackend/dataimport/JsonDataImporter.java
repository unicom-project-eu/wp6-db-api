package it.datawizard.unicom.unicombackend.dataimport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JsonDataImporter {
    private final Logger LOG = LoggerFactory.getLogger(JsonDataImporter.class);

    public void importData(File jsonFile) throws IOException {
        LOG.info("Importing " + jsonFile.getPath());
    }

    public ArrayList<PackagedMedicinalProduct> parseDataJsonString(String jsonData) throws IOException {
        return new ObjectMapper().readValue(jsonData, new TypeReference<>() {});
    }

    protected void saveParsedPackagedMedicinalProduct(ArrayList<PackagedMedicinalProduct> packagedMedicinalProduct) {
        throw new NotImplementedException();
    }
}
