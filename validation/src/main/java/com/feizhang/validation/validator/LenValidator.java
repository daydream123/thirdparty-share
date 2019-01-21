package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.Len;

/**
 * Validator on String object.
 */
public class LenValidator extends ConstraintValidator<Len> {

    public LenValidator(Len annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null){
            return false;
        }


        if (value instanceof String) {
            int min = annotation.min();
            int max = annotation.max();
            int len = ((String) value).length();
            return len >= min && len <= max;
        }

        throw new IllegalArgumentException(value + " is not a string type");
    }

    @Override
    public String getMessage(Object value) {
        if (value == null) {
            return buildMessage(annotation.nullMessage());
        } else {
            return buildMessage(annotation.lenMessage());
        }
    }

    private String buildMessage(String message){
        if (message.contains("%")) {
            return String.format(message, fieldName, annotation.min(), annotation.max());
        } else {
            return message;
        }
    }
}
