package it.datawizard.unicom.unicombackend.dataimport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataImport implements Runnable {
    public enum ImportableCountries {
        finland,
        belgium,
    }

    private static Logger LOG = LoggerFactory.getLogger(DataImport.class);
    ImportableCountries country;
    public DataImport(ImportableCountries country) {
        this.country = country;
    }

    @Override
    public void run() {
        LOG.info("Started data import for \"" + this.country.toString() + "\"");

        switch (this.country) {
            case belgium -> new BelgiumDataImport().run();
            case finland -> new FinlandDataImport().run();
        }
    }
}