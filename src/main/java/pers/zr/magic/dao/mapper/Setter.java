package pers.zr.magic.dao.mapper;

import java.lang.reflect.Method;

/**
 * Created by zhurong on 2016-5-6.
 */
public class Setter {

    private String fieldName;

    private Class<?> fieldType;

    private Method setMethod;

    public Setter(String fieldName, Class<?> fieldType, Method setMethod) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.setMethod = setMethod;
    }

    public String getFieldName() {
        return fieldName;
    }


    public Class<?> getFieldType() {
        return fieldType;
    }


    public Method getSetMethod() {
        return setMethod;
    }

}
