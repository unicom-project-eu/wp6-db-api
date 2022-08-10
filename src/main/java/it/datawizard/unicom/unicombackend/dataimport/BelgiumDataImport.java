package it.datawizard.unicom.unicombackend.dataimport;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.datawizard.unicom.unicombackend.dataimport.csvbeans.BelgiumCsvBean;
import it.datawizard.unicom.unicombackend.dataimport.csvbeans.CsvBean;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BelgiumDataImport implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(BelgiumDataImport.class);

    @Override
    public void run() {
        LOG.info("Reading CSV");

        try {
            ClassPathResource csvResource = new ClassPathResource("dataimport/belgium.csv");

            List<CsvBean> belgiumCsvRecordBeans = CsvBean.parseFile(csvResource.getFile().toPath(), BelgiumCsvBean.class);

            LOG.info("Parsed " + belgiumCsvRecordBeans.size() + " csv records");
        } catch (Exception e) {
            LOG.error(e.toString());
            System.exit(1);
        }

        System.exit(0);
    }

    public static List<CsvBean> beanBuilderExample(Path path, Class<? extends CsvBean> clazz) throws Exception {
        List<CsvBean> result;
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CsvBean> cb = new CsvToBeanBuilder<CsvBean>(reader)
                    .withType(clazz)
                    .build();

            result = cb.parse();
        }

        return result;
    }

}
