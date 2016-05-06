package pers.zr.magic.dao.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

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
    private Map<String, Field> columnAndFieldMap;

    public GenericMapper(Class<ENTITY> entityClass, Map<String, Field> columnAndFieldMap) {
        this.entityClass = entityClass;
        this.columnAndFieldMap = columnAndFieldMap;
    }

    private static Map<Class<?>, Map<Field, Method>> settersMap = new HashMap<Class<?>, Map<Field, Method>>();

    private static Map<Class<?>, Map<Field, Method>> gettersMap = new HashMap<Class<?>, Map<Field, Method>>();

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
            methodMap.put(field, method);
        }
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
            for(int k=0; k<meta.getColumnCount(); k++) {
                String column = meta.getColumnName(k);
                Method setMethod = getMethod(entityClass, columnAndFieldMap.get(column), MethodType.SET);
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
            e.printStackTrace();
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
