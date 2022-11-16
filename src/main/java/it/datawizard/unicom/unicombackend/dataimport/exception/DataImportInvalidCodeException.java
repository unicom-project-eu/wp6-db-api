package it.datawizard.unicom.unicombackend.dataimport.exception;

public class DataImportInvalidCodeException extends DataImportException {
    public DataImportInvalidCodeException(String code, Class<?> codingEntity) {
        super("Invalid code '" + code + "' for entity " + codingEntity.getName());
    }
}
