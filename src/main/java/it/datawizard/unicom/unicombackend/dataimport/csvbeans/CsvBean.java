package it.datawizard.unicom.unicombackend.dataimport.csvbeans;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvBean {
    public static List<CsvBean> parseFile(Path path, Class<? extends CsvBean> clazz) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CsvBean> cb = new CsvToBeanBuilder<CsvBean>(reader)
                    .withType(BelgiumCsvBean.class)
                    .build();

            return cb.parse();
        }
    }
}
