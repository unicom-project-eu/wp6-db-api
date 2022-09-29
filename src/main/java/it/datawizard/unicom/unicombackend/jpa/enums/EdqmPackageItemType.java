package it.datawizard.unicom.unicombackend.jpa.enums;

import lombok.Getter;

@Getter
public enum EdqmPackageItemType implements IEdqmEnum {
    ;
    private final String code;
    private final String term;

    EdqmPackageItemType(String code, String term) {
        this.code = code;
        this.term = term;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
