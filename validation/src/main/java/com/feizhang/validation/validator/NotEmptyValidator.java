package com.feizhang.validation.validator;

import com.feizhang.validation.annotations.NotEmpty;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Validator on collection object.
 */
public class NotEmptyValidator extends ConstraintValidator<NotEmpty> {

    public NotEmptyValidator(NotEmpty annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Collection) {
            return !((Collection) value).isEmpty();
        }

        if (value instanceof Map) {
            return !((Map) value).isEmpty();
        }

        if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            return array.length > 0;
        }

        throw new IllegalArgumentException(value + " is not a collection type");
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
