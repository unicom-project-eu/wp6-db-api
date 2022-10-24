package it.datawizard.unicom.unicombackend.jpa.specification;

import java.text.MessageFormat;

public abstract class AbstractSpecifications {
    protected static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}
