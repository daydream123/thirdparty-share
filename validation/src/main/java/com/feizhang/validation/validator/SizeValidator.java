package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.Size;

/**
 * Validator on number object.
 */
public class SizeValidator extends ConstraintValidator<Size> {

    public SizeValidator(Size annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        int minValue = annotation.min();
        int maxValue = annotation.max();

        if (value instanceof Number) {
            int number = ((Number) value).intValue();
            return number >= minValue && number <= maxValue;
        }

        throw new IllegalArgumentException(value + " is not a number type");
    }

    @Override
    public String getMessage(Object object) {
        String message = annotation.message();
        if (message.contains("%")) {
            return String.format(annotation.message(), fieldName,
                    annotation.min(), annotation.max());
        } else {
            return message;
        }
    }

}
