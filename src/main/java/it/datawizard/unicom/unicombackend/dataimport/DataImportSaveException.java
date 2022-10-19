package it.datawizard.unicom.unicombackend.dataimport;

import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import lombok.Getter;

@Getter
public class DataImportSaveException extends Exception {
    private final PackagedMedicinalProduct packagedMedicinalProduct;

    public DataImportSaveException(Throwable throwable, PackagedMedicinalProduct packagedMedicinalProduct) {
        super(throwable);
        this.packagedMedicinalProduct = packagedMedicinalProduct;
    }
}
