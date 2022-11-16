package it.datawizard.unicom.unicombackend.dataimport.exception;

import it.datawizard.unicom.unicombackend.jpa.entity.PackagedMedicinalProduct;
import lombok.Getter;

@Getter
public class DataImportSaveException extends DataImportException {
    private final PackagedMedicinalProduct packagedMedicinalProduct;

    public DataImportSaveException(Throwable throwable, PackagedMedicinalProduct packagedMedicinalProduct) {
        super(throwable);
        this.packagedMedicinalProduct = packagedMedicinalProduct;
    }
}
