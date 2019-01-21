package com.feizhang.validation.validator;

import java.lang.annotation.Annotation;

public abstract class ConstraintValidator<A extends Annotation> {
    protected A annotation;
    String fieldName;

    ConstraintValidator(A annotation, String fieldName){
        this.annotation = annotation;
        this.fieldName = fieldName;
    }

    public abstract boolean isValid(Object value);

    public abstract String getMessage(Object value);
}
