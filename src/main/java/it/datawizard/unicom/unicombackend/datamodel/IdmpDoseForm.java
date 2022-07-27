package it.datawizard.unicom.unicombackend.datamodel;

public enum IdmpDoseForm {
    tablets("Tablets"),
    liquidFilledCapsule("Liquid Filled Capsule");

    private final String text;

    IdmpDoseForm(String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
