package it.datawizard.unicom.unicombackend.dataimport.exception;

public class DataImportException extends RuntimeException {
    public DataImportException(String message) {
        super(message);
    }

    public DataImportException(Throwable e) {
        super(e);
    }
}
