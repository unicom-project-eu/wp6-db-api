package it.datawizard.unicom.unicombackend.dataimport;

import com.opencsv.bean.CsvBindByName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BelgiumDataImport implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(BelgiumDataImport.class);

    @Override
    public void run() {

    }
}

class BelgiumDataBean extends CsvBean {
    @CsvBindByName

}