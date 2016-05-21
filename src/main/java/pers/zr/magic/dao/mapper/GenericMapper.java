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

    @Override
    public ENTITY mapRow(ResultSet rs, int i) throws SQLException {
        ENTITY entity;
        try {
            entity = this.entityClass.newInstance();
            ResultSetMetaData meta = rs.getMetaData();
            for(int k=1; k<=meta.getColumnCount(); k++) {
                String column = meta.getColumnName(k);
                Field field = MapperContextHolder.getFieldWithColumn(entityClass, column);
                if(field == null) {
                    continue;
                }
                Method setMethod = MapperContextHolder.getMethod(entityClass, field, MethodType.SET);
                if(setMethod == null) {
                    continue;
                }
                Object value = getValue4Type(rs, column, setMethod.getParameterTypes()[0]);
                if(value != null) {
                    setMethod.invoke(entity, value);
                }
            }
        } catch (Exception e) {
            log.error("Failed to bind data to entity", e);
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
