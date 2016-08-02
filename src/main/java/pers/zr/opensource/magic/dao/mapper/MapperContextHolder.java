package pers.zr.opensource.magic.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import pers.zr.opensource.magic.dao.constants.MethodType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MapperContextHolder {

    /** class-rowMapper*/
    private static Map<Class<?>, RowMapper<?>> rowMapperMap = new HashMap<Class<?>, RowMapper<?>>();

    /** class-field-setMethod*/
    private static Map<Class<?>, Map<Field, Method>> settersMap = new HashMap<Class<?>, Map<Field, Method>>();

    /** class-field-getMethod*/
    private static Map<Class<?>, Map<Field, Method>> gettersMap = new HashMap<Class<?>, Map<Field, Method>>();

    /** class-column-field*/
    private static Map<Class<?>, Map<String, Field>> fieldsMap = new HashMap<Class<?>, Map<String, Field>>();

    public static void setRowMapper(Class<?> clazz, RowMapper<?> rowMapper) {
        rowMapperMap.put(clazz, rowMapper);
    }

    public static RowMapper<?> getRowMapper(Class<?> clazz) {
        return rowMapperMap.get(clazz);
    }

    public static void setFieldWithColumn(Class<?> clazz, String column, Field field) {
        Map<String, Field> colFieldMap = fieldsMap.get(clazz);
        if(null == colFieldMap) {
            colFieldMap = new HashMap<String, Field>();
            fieldsMap.put(clazz, colFieldMap);
        }
        colFieldMap.put(column, field);
    }

    public static Field getFieldWithColumn(Class<?> clazz, String column) {
        Field field = null;
        Map<String, Field> colFieldMap = fieldsMap.get(clazz);
        if(null != colFieldMap) {
            field = colFieldMap.get(column);
        }
        return field;
    }

    public static Method getMethod(Class<?> clazz, Field field, MethodType type) {
        Method method = null;
        Map<Field, Method> methodMap = getMethodMap(clazz, field, type);
        if(methodMap != null) {
            method = methodMap.get(field);
        }
        return method;

    }

    public static void setMethod(Class<?> clazz, Field field, Method method, MethodType type) {
        Map<Field, Method> methodMap = getMethodMap(clazz, field, type);
        if(methodMap == null) {
            methodMap = new HashMap<Field, Method>();
            if(MethodType.GET.equals(type)) {
                gettersMap.put(clazz, methodMap);

            }else if(MethodType.SET.equals(type)) {
                settersMap.put(clazz, methodMap);

            }else {
                throw new RuntimeException("Invalid Method type");
            }

        }
        methodMap.put(field, method);
    }


    private static Map<Field, Method> getMethodMap(Class<?> clazz, Field field, MethodType type) {
        Map<Field, Method> methodMap;
        if(MethodType.GET.equals(type)) {
            methodMap = gettersMap.get(clazz);

        }else if(MethodType.SET.equals(type)) {
            methodMap = settersMap.get(clazz);

        }else {
            throw new RuntimeException("Invalid Method type");
        }

        return methodMap;
    }


}
