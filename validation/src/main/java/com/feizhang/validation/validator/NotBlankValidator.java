package com.feizhang.validation.validator;

import android.text.TextUtils;
import com.feizhang.validation.annotations.NotBlank;

/**
 * Validator on String object.
 */
public class NotBlankValidator extends ConstraintValidator<NotBlank> {

    public NotBlankValidator(NotBlank annotation, String fieldName) {
        super(annotation, fieldName);
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof String){
            return !TextUtils.isEmpty((CharSequence) value);
        } else {
            throw new IllegalArgumentException(value + " is not a string type");
        }
    }

    @Override
    public String getMessage(Object object) {
        String message = annotation.message();
        if (message.contains("%")){
            return String.format(annotation.message(), fieldName);
        } else {
            return message;
        }
    }
}
