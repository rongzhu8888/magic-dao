package pers.zr.magic.dao.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import pers.zr.magic.dao.constants.MethodType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by zhurong on 2016-5-6.
 */
public class GenericMapper<ENTITY extends Serializable> implements RowMapper<ENTITY> {

    private static Logger log = LogManager.getLogger(GenericMapper.class);

    private Class<ENTITY> entityClass;

    public GenericMapper(Class<ENTITY> entityClass) {
        this.entityClass = entityClass;
    }

    private static Map<Class<?>, Map<Field, Method>> settersMap = new HashMap<Class<?>, Map<Field, Method>>();

    private static Map<Class<?>, Map<Field, Method>> gettersMap = new HashMap<Class<?>, Map<Field, Method>>();

    private static Map<Class<?>, Map<String, Field>> fieldsMap = new HashMap<Class<?>, Map<String, Field>>();


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



    @Override
    public ENTITY mapRow(ResultSet rs, int i) throws SQLException {
        ENTITY entity;
        try {
            entity = this.entityClass.newInstance();
            ResultSetMetaData meta = rs.getMetaData();
            for(int k=1; k<=meta.getColumnCount(); k++) {
                String column = meta.getColumnName(k);
                Method setMethod = getMethod(entityClass, getFieldWithColumn(entityClass, column), MethodType.SET);
                Object value = getValue4Type(rs, column, setMethod.getParameterTypes()[0]);
                if(value != null) {
                    setMethod.invoke(entity, value);
                }
            }
        } catch (Exception e) {
            log.error("error when bind data");
            throw new SQLException(e);
        }
        return entity;
    }


    public static Object getValue4Type(ResultSet rs, String column, Class<?> typeClass) throws SQLException {

        if (Collection.class.isAssignableFrom(typeClass)) {
            return null;
        }

        try {
            rs.findColumn(column);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        if (typeClass.equals(Integer.class) || typeClass.equals(Integer.TYPE)) {
            return rs.getInt(column);
        }

        if (typeClass.equals(Long.class) || typeClass.equals(Long.TYPE)) {
            return rs.getLong(column);
        }

        if (typeClass.equals(Boolean.class) || typeClass.equals(Boolean.TYPE)) {
            return rs.getBoolean(column);
        }

        if (typeClass.equals(Float.class) || typeClass.equals(Float.TYPE)) {
            return rs.getFloat(column);
        }

        if (typeClass.equals(Double.class) || typeClass.equals(Double.TYPE)) {
            return rs.getDouble(column);
        }

        if (typeClass.equals(Byte.class) || typeClass.equals(Byte.TYPE)) {
            return rs.getByte(column);
        }

        if (typeClass.equals(String.class)) {
            return rs.getString(column);
        }

        if (Date.class.isAssignableFrom(typeClass)) {
            return rs.getTimestamp(column);
        }

        if (java.sql.Date.class.isAssignableFrom(typeClass)) {
            return rs.getDate(column);
        }

        return rs.getObject(column);
    }
}
