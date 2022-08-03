package it.datawizard.unicom.unicombackend.dataimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataImport implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(DataImport.class);
    String country;
    public DataImport(String country) {
        this.country = country;
    }

    @Override
    public void run() {
        LOG.info("Running data import for " + this.country);
    }
}