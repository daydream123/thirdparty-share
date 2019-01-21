package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.NotNull;

/**
 * Validator on not primary object.
 */
public class NotNullValidator extends ConstraintValidator<NotNull> {

    public NotNullValidator(NotNull annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        return value != null;
    }

    @Override
    public String getMessage(Object object) {
        String message = annotation.message();
        if (message.contains("%")) {
            return String.format(annotation.message(), fieldName);
        } else {
            return message;
        }
    }

}
