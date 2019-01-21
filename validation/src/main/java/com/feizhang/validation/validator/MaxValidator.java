package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.Max;

/**
 * Validator on number object.
 */
public class MaxValidator extends ConstraintValidator<Max> {

    public MaxValidator(Max annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        int maxValue = annotation.max();

        if (value instanceof Number) {
            return ((Number) value).intValue() <= maxValue;
        }

        throw new IllegalArgumentException(value + " is not a number type");
    }

    @Override
    public String getMessage(Object object) {
        String message = annotation.message();
        if (message.contains("%")) {
            return String.format(annotation.message(), fieldName, annotation.max());
        } else {
            return message;
        }
    }

}
