package com.feizhang.validation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.feizhang.validation.annotations.Len;
import com.feizhang.validation.annotations.Max;
import com.feizhang.validation.annotations.Min;
import com.feizhang.validation.annotations.NotBlank;
import com.feizhang.validation.annotations.NotEmpty;
import com.feizhang.validation.annotations.NotNull;
import com.feizhang.validation.annotations.Size;
import com.feizhang.validation.validator.ConstraintValidator;
import com.feizhang.validation.validator.LenValidator;
import com.feizhang.validation.validator.MaxValidator;
import com.feizhang.validation.validator.MinValidator;
import com.feizhang.validation.validator.NotBlankValidator;
import com.feizhang.validation.validator.NotEmptyValidator;
import com.feizhang.validation.validator.NotNullValidator;
import com.feizhang.validation.validator.SizeValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * A validator to validate fields of Object with Annotations(@Max, @Min, @NotEmpty, @NotNull, @Rang, etc.),
 * if someone field's value did't pass the validations, it'll toast a message specified by its annotation.
 */
public class Validator {
    private static final String TAG = "Validator";

    /**
     * Validate fields under object.
     */
    public static boolean validate(Context context, Object object) {
        if (object == null) {
            return false;
        }

        // ignore String and Number
        if (object instanceof String || object instanceof Number) {
            throw new IllegalArgumentException("String or Number instance is not supported");
        }

        // check fields in java.util.List
        if (object instanceof List) {
            List list = (List) object;
            for (Object item : list) {
                boolean valid = validate(context, item);
                if (!valid) {
                    return false;
                }
            }

            return true;
        }

        // check keySet and values in map
        if (object instanceof Map){
            Map map = (Map) object;

            // validate keys
            for (Object next : map.keySet()) {
                boolean valid = validateObject(context, next);
                if (!valid) {
                    return false;
                }
            }

            // validate values
            for (Object next : map.values()){
                boolean valid = validateObject(context, next);
                if (!valid) {
                    return false;
                }
            }

            return true;
        }

        // check fields in array
        if (object.getClass().isArray()) {
            Object[] array = (Object[]) object;
            for (Object item : array) {
                boolean valid = validate(context, item);
                if (!valid) {
                    return false;
                }
            }

            return true;
        }

        // check fields of object
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // ignore static and final
            int modifiers = field.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            boolean isFinal = Modifier.isFinal(modifiers);
            if (isStatic || isFinal) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object value = field.get(object);
                Annotation[] annotations = field.getAnnotations();
                if (annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        // validate object its self
                        boolean valid = validateWithAnnotation(context, annotation, value, field.getName());
                        if (!valid) {
                            return false;
                        }
                    }
                }

                // validate fields of object, but make sure it's not String or Number
                boolean valid = validateObject(context, value);
                if (!valid) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * Before validation make sure it's not String or Number
     */
    private static boolean validateObject(Context context, Object object){
        if (object != null && !(object instanceof String) && !(object instanceof Number)) {
            return validate(context, object);
        }

        return true;
    }

    /**
     * Validate value with annotations that we support.
     *
     * @param context    Android context
     * @param annotation annotation to validate with
     * @param object     object to validate
     * @return return true when the object passes validation
     * or the annotation we don't support
     */
    private static <A extends Annotation> boolean validateWithAnnotation(Context context,
                                                                         A annotation,
                                                                         Object object,
                                                                         String fieldName) {
        ConstraintValidator validator = null;
        if (annotation instanceof NotNull) {
            validator = new NotNullValidator((NotNull) annotation, fieldName);
        } else if (annotation instanceof NotEmpty) {
            validator = new NotEmptyValidator((NotEmpty) annotation, fieldName);
        } else if (annotation instanceof Min) {
            validator = new MinValidator((Min) annotation, fieldName);
        } else if (annotation instanceof Max) {
            validator = new MaxValidator((Max) annotation, fieldName);
        } else if (annotation instanceof Size) {
            validator = new SizeValidator((Size) annotation, fieldName);
        } else if (annotation instanceof Len){
            validator = new LenValidator((Len) annotation, fieldName);
        } else if (annotation instanceof NotBlank){
            validator = new NotBlankValidator((NotBlank) annotation, fieldName);
        }

        if (validator != null) {
            boolean valid = validator.isValid(object);
            if (!valid) {
                Log.e(TAG, validator.getMessage(object));
                Toast.makeText(context, validator.getMessage(object), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}
