package it.datawizard.unicom.unicombackend.dataimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JsonDataImporter {
    private final Logger LOG = LoggerFactory.getLogger(JsonDataImporter.class);

    public void importData(File jsonFile) {
        LOG.info("Importing " + jsonFile.getPath());
    }
}
