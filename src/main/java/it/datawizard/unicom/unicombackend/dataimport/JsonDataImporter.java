package it.datawizard.unicom.unicombackend.dataimport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import it.datawizard.unicom.unicombackend.jpa.entity.PharmaceuticalProduct;
import it.datawizard.unicom.unicombackend.jpa.repository.PharmaceuticalProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JsonDataImporter {
    private final static Logger LOG = LoggerFactory.getLogger(JsonDataImporter.class);

    private final PharmaceuticalProductRepository pharmaceuticalProductRepository;

    @Autowired
    public JsonDataImporter(PharmaceuticalProductRepository pharmaceuticalProductRepository) {
        this.pharmaceuticalProductRepository = pharmaceuticalProductRepository;
    }

    public void importData(File jsonFile) throws IOException {
        LOG.info("Importing " + jsonFile.getPath());
    }

    public ArrayList<PackagedMedicinalProduct> parseDataJsonString(String jsonData) throws IOException {
        return new ObjectMapper().readValue(jsonData, new TypeReference<>() {});
    }

    @Transactional
    protected void saveParsedPackagedMedicinalProducts(ArrayList<PackagedMedicinalProduct> packagedMedicinalProducts) {
        for (PackagedMedicinalProduct packagedMedicinalProduct : packagedMedicinalProducts) {
            // PharmaceuticalProduct
            PharmaceuticalProduct pharmaceuticalProduct = packagedMedicinalProduct.getMedicinalProduct()
                    .getPharmaceuticalProduct();

            pharmaceuticalProductRepository.save(pharmaceuticalProduct);
        }

        throw new NotImplementedException();
    }
}
