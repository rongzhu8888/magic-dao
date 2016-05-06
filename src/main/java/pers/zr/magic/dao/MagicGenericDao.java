package pers.zr.magic.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.action.*;
import pers.zr.magic.dao.annotation.Column;
import pers.zr.magic.dao.annotation.Key;
import pers.zr.magic.dao.annotation.Shard;
import pers.zr.magic.dao.annotation.Table;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.mapper.GenericMapper;
import pers.zr.magic.dao.mapper.MethodType;
import pers.zr.magic.dao.matcher.EqualsMatcher;
import pers.zr.magic.dao.matcher.Matcher;
import pers.zr.magic.dao.shard.ShardStrategy;
import pers.zr.magic.dao.utils.ClassUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by zhurong on 2016-4-29.
 */
public abstract class MagicGenericDao<KEY extends Serializable, ENTITY extends Serializable> implements MagicDao<KEY, ENTITY> {

    protected final Logger log = LogManager.getLogger(getClass());

    protected MagicDataSource dataSource;

    public void setDataSource(MagicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Class<ENTITY> entityClass;

    private Class<KEY> keyClass;

    private List<String> keys;
    private List<Field> keyFields;
    private List<String> columns;

    private ActionTable table;

    private ShardStrategy shardStrategy;

    protected RowMapper<ENTITY> rowMapper;


    @SuppressWarnings("unchecked")
    public MagicGenericDao() {

        //获取实体类和主键类
        Type[] types = ClassUtil.getGenericTypes(getClass());
        keyClass = (Class<KEY>)types[0];
        entityClass = (Class<ENTITY>)types[1];

        //获取实体类对应的表
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if(tableAnnotation == null) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] must be annotated with @Table");
        }

        Set<Field> fields = ClassUtil.getAllFiled(entityClass);
        if(CollectionUtils.isEmpty(fields)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no fields");
        }

        Set<Method> methods = ClassUtil.getAllMethod(entityClass);
        if(CollectionUtils.isEmpty(methods)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no methods");
        }

        //获取列、主键
        keys = new ArrayList<String>();
        keyFields = new ArrayList<Field>();
        columns = new ArrayList<String>();

        Map<String, Field> columnAndFieldMap = new HashMap<String, Field>();
        for(Field field : fields) {

            Key keyAnnotation = field.getAnnotation(Key.class);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if(null != keyAnnotation) {
                keys.add(keyAnnotation.column());
                keyFields.add(field);
                columns.add(keyAnnotation.column());
                columnAndFieldMap.put(keyAnnotation.column(), field);
            }else if(columnAnnotation != null) {
                columns.add(columnAnnotation.value());
                columnAndFieldMap.put(columnAnnotation.value(), field);
            }

            //获取各属性对应的SET\GET方法
            String fieldName = field.getName();
            for(Method method : methods) {
                String methodName = method.getName();
                if(methodName.equalsIgnoreCase("set" + fieldName) ||
                        (fieldName.startsWith("is") && methodName.equalsIgnoreCase("set" + fieldName.substring(2)))) {

                    GenericMapper.setMethod(entityClass, field, method, MethodType.SET);

                }else if(methodName.equalsIgnoreCase("get" + fieldName) ||
                        methodName.equalsIgnoreCase("is" + fieldName) ||
                        (fieldName.startsWith("is") && methodName.equalsIgnoreCase(fieldName))) {

                    GenericMapper.setMethod(entityClass, field, method, MethodType.GET);

                }

            }
        }
        table = new ActionTable();
        table.setTableName(tableAnnotation.name());
        table.setKeys(keys.toArray(new String[keys.size()]));

        rowMapper = new GenericMapper<ENTITY>(entityClass, columnAndFieldMap);

        //获取分表策略
        Shard shardAnnotation = entityClass.getAnnotation(Shard.class);
        if(null != shardAnnotation) {
            int shardCount = shardAnnotation.shardCount();
            String shardColumn = shardAnnotation.shardColumn();
            String separator = shardAnnotation.separator();
            shardStrategy = new ShardStrategy(shardCount, shardColumn, separator);
        }

    }


    @Override
    public ENTITY get(KEY value) {

        QueryBuilder queryBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.QUERY);
        if(null == queryBuilder) {
            if(null == shardStrategy) {
                queryBuilder = new QueryBuilder(table);
            }else {
                queryBuilder = new QueryBuilder(table, shardStrategy);
            }
            ActionBuilderContainer.setActionBuilder(queryBuilder);
        }

        Query query = queryBuilder.build();
        query.setQueryFields(columns.toArray(new String[columns.size()]));

        if(keys.size() == 1) { //单一主键
            Matcher matcher = new EqualsMatcher(table.getKeys()[0], value);
            query.addCondition(matcher);
        }else { //组合主键
            for(int i=0; i<keyFields.size(); i++) {

                Method fieldGetMethod = GenericMapper.getMethod(entityClass, keyFields.get(i), MethodType.GET);
                try {
                    String keyColumn = keys.get(i);
                    Object keyValue = fieldGetMethod.invoke(this);
                    query.addCondition(new EqualsMatcher(keyColumn, keyValue));
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage(), e.getTargetException());
                }
            }

        }

        List<ENTITY> list = dataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
//
//        return CollectionUtils.isEmpty(list) ? null : list.get(0);

        return null;
    }

    @Override
    public void insert(ENTITY entity){

        //忽略autoIncrement
        //忽略readOnly
    }


    @Override
    public KEY insertAndGetKey(ENTITY entity){
        return null;
    }


    @Override
    public void updateByKey(ENTITY entity) {}


    @Override
    public void deleteByKey(KEY key){}



//    private boolean isCombinedKey(Class<KEY> keyClass) {
//
//        if (keyClass.equals(Integer.class) || keyClass.equals(Integer.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(Long.class) || keyClass.equals(Long.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(Boolean.class) || keyClass.equals(Boolean.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(Float.class) || keyClass.equals(Float.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(Double.class) || keyClass.equals(Double.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(Byte.class) || keyClass.equals(Byte.TYPE)) {
//            return false;
//        }
//
//        if (keyClass.equals(String.class)) {
//            return false;
//        }
//
//        if (java.util.Date.class.isAssignableFrom(keyClass)) {
//            return false;
//        }
//
//        return true;
//    }
}
