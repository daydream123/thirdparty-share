package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.Min;

/**
 * Validator on number object.
 */
public class MinValidator extends ConstraintValidator<Min> {

    public MinValidator(Min annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        int minValue = annotation.min();

        if (value instanceof Number) {
            return ((Number) value).intValue() >= minValue;
        }

        throw new IllegalArgumentException(value + " is not a number type");
    }


    @Override
    public String getMessage(Object object) {
        String message = annotation.message();
        if (message.contains("%")) {
            return String.format(annotation.message(), fieldName, annotation.min());
        } else {
            return message;
        }
    }
}
