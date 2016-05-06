package pers.zr.magic.dao.mapper;

import java.lang.reflect.Method;

/**
 * Created by zhurong on 2016-5-6.
 */
public class Getter {

    private String fieldName;

    private Class<?> fieldType;

    private Method getMethod;

    public Getter(String fieldName, Class<?> fieldType, Method getMethod) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.getMethod = getMethod;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public Method getGetMethod() {
        return getMethod;
    }
}
