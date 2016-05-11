package pers.zr.magic.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.action.*;
import pers.zr.magic.dao.annotation.Column;
import pers.zr.magic.dao.annotation.Key;
import pers.zr.magic.dao.annotation.Shard;
import pers.zr.magic.dao.annotation.Table;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.mapper.GenericMapper;
import pers.zr.magic.dao.constants.MethodType;
import pers.zr.magic.dao.matcher.EqualsMatcher;
import pers.zr.magic.dao.matcher.Matcher;
import pers.zr.magic.dao.order.Order;
import pers.zr.magic.dao.page.PageModel;
import pers.zr.magic.dao.shard.ShardStrategy;
import pers.zr.magic.dao.utils.ClassUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    /** 实体类 */
    private Class<ENTITY> entityClass;

    /** 主键类 */
    private Class<KEY> keyClass;

    /** 表中与实体主键属性对应的字段列表 */
    private List<String> keyColumns;

    /** 实体中主键属性列表*/
    private List<Field> keyFields;

    /** 表中与实体属性对应的字段列表*/
    private List<String> tableColumns;

    /** 表中与实体属性对应且需要insert的字段列表*/
    private List<String> toInsertColumns;

    /** 表中与实体属性对应且需要update的字段列表*/
    private List<String> toUpdateColumns;

    /** 表 */
    private ActionTable table;

    /** 分表策略 */
    private ShardStrategy shardStrategy;

    /** 数据映射对象 */
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
            throw new RuntimeException("Class [" + entityClass.getName() +"] must be annotated with @Table!");
        }

        Set<Field> fields = ClassUtil.getAllFiled(entityClass);
        if(CollectionUtils.isEmpty(fields)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no fields!");
        }

        Set<Method> methods = ClassUtil.getAllMethod(entityClass);
        if(CollectionUtils.isEmpty(methods)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no methods!");
        }

        //获取列、主键
        keyColumns = new ArrayList<String>();
        keyFields = new ArrayList<Field>();
        tableColumns = new ArrayList<String>();
        toInsertColumns= new ArrayList<String>();

        for(Field field : fields) {

            Key keyAnnotation = field.getAnnotation(Key.class);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if(null != keyAnnotation) {
                keyColumns.add(keyAnnotation.column());
                keyFields.add(field);

                tableColumns.add(keyAnnotation.column());

                //非自增主键值需要写入
                if(!keyAnnotation.autoIncrement()) {
                    toInsertColumns.add(keyAnnotation.column());
                }

                GenericMapper.setFieldWithColumn(entityClass, keyAnnotation.column(), field);

            }else if(columnAnnotation != null) {
                tableColumns.add(columnAnnotation.value());

                //非只读字段需要写入
                if(!columnAnnotation.readOnly()) {
                    toInsertColumns.add(columnAnnotation.value());
                }
                GenericMapper.setFieldWithColumn(entityClass, columnAnnotation.value(), field);
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
        table.setKeys(keyColumns.toArray(new String[keyColumns.size()]));
        table.setColumns(tableColumns.toArray(new String[tableColumns.size()]));

        //获取可更新的字段=【toInsertColumns】-【keyColumns】
        toUpdateColumns = new ArrayList<String>();
        for(String column : toInsertColumns) {
            if(!keyColumns.contains(column)) {
                toUpdateColumns.add(column);
            }
        }

        //初始化数据映射对象
        rowMapper = new GenericMapper<ENTITY>(entityClass);

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
    public ENTITY get(KEY key) {

        Query query = getBuilder(ActionMode.QUERY).build();
        query.setQueryFields(tableColumns);
        query.addConditions(getKeyConditions(key));

        List<ENTITY> list = dataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);

    }

    @Override
    public void insert(ENTITY entity){

        Insert insert = getBuilder(ActionMode.INSERT).build();
        insert.setInsertFields(getDataMapByColumns(toInsertColumns, entity));

        dataSource.getJdbcTemplate(ActionMode.INSERT).update(insert.getSql(), insert.getParams());
    }


    @Override
    public Long insertForId(ENTITY entity){

        Insert insert = getBuilder(ActionMode.INSERT).build();
        Map<String, Object> dataMap = getDataMapByColumns(toInsertColumns, entity);
        insert.setInsertFields(dataMap);
        String sql = insert.getSql();

        String[] columnsArray = new String[dataMap.size()];
        Object[] paramsArray = new Object[dataMap.size()];
        int i=0;
        for(Map.Entry<String, Object> entry : dataMap.entrySet()) {
            columnsArray[i] = entry.getKey();
            paramsArray[i] = entry.getValue();
            i++;
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(ActionMode.INSERT);
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = jdbcTemplate.getDataSource().getConnection()
                                .prepareStatement(sql, columnsArray);
                        for(int k=1; k<=paramsArray.length; k++) {
                            ps.setObject(k, paramsArray[k-1]);
                        }
                        return ps;
                    }
                }, keyHolder);


        return keyHolder.getKey().longValue();
    }


    @Override
    public void update(ENTITY entity) {

        Update update = getBuilder(ActionMode.UPDATE).build();
        update.addConditions(getKeyConditionsFomEntity(entity));
        update.setUpdateFields(getDataMapByColumns(toUpdateColumns, entity));

        dataSource.getJdbcTemplate(ActionMode.UPDATE).update(update.getSql(), update.getParams());


    }


    @Override
    public void delete(KEY key){

        Delete delete = getBuilder(ActionMode.DELETE).build();
        delete.addConditions(getKeyConditions(key));

        dataSource.getJdbcTemplate(ActionMode.DELETE).update(delete.getSql(), delete.getParams());

    }

    @Override
    public void delete(Matcher...conditions) {

        Delete delete = getBuilder(ActionMode.DELETE).build();
        if(null != conditions && conditions.length >0) {
            delete.addConditions(Arrays.asList(conditions));
        }

        dataSource.getJdbcTemplate(ActionMode.DELETE).update(delete.getSql(), delete.getParams());
    }

    @Override
    public void update(Map<String, Object> valueMap, Matcher...conditions) {

        Update update = getBuilder(ActionMode.UPDATE).build();
        if(null != conditions && conditions.length >0) {
            update.addConditions(Arrays.asList(conditions));
        }
        update.setUpdateFields(valueMap);

        dataSource.getJdbcTemplate(ActionMode.UPDATE).update(update.getSql(), update.getParams());
    }

    @Override
    public List<ENTITY> query(Map<String, Object> conditions, Order... orders){

        return query(conditions, null, orders);
    }

    @Override
    public List<ENTITY> query(Map<String, Object> conditions, PageModel pageModel, Order... orders) {

        Query query = getBuilder(ActionMode.QUERY).build();
        query.setQueryFields(tableColumns);
        for(Map.Entry<String, Object> entry : conditions.entrySet()) {
            query.addCondition(new EqualsMatcher(entry.getKey(), entry.getValue()));
        }
        query.setOrders(orders);
        query.setPageModel(pageModel);

        List<ENTITY> list = dataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<ENTITY>() : list;
    }

    public List<ENTITY> query(List<Matcher> conditions, Order... orders) {

        return query(conditions, null, orders);

    }

    public List<ENTITY> query(List<Matcher> conditions, PageModel pageModel, Order...orders) {
        Query query = getBuilder(ActionMode.QUERY).build();
        query.setQueryFields(tableColumns);
        query.addConditions(conditions);
        query.setOrders(orders);
        query.setPageModel(pageModel);
        List<ENTITY> list = dataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<ENTITY>() : list;
    }

    @Override
    public Long getCount(Map<String, Object> conditions) {

        Query query = getBuilder(ActionMode.QUERY).build();
        query.setQueryFields(Collections.singletonList("COUNT(1)"));
        for(Map.Entry<String, Object> entry : conditions.entrySet()) {
            query.addCondition(new EqualsMatcher(entry.getKey(), entry.getValue()));
        }

        return dataSource.getJdbcTemplate(ActionMode.QUERY).queryForObject(query.getSql(), query.getParams(), Long.class);
    }

    public Long getCount(List<Matcher> conditions) {
        Query query = getBuilder(ActionMode.QUERY).build();
        query.setQueryFields(Collections.singletonList("COUNT(1)"));
        query.addConditions(conditions);

        return dataSource.getJdbcTemplate(ActionMode.QUERY).queryForObject(query.getSql(), query.getParams(), Long.class);
    }

    private List<Matcher> getKeyConditions(KEY key) {
        List<Matcher> matcherList = new ArrayList<Matcher>();

        if(CollectionUtils.isEmpty(keyColumns)) {
            throw new RuntimeException("no key columns found!");
        }

        if(keyColumns.size() == 1 && ClassUtil.isBasicType(keyClass)) { //单一基本类型主键（如Long,String,int等）,直接取key值
            matcherList.add(new EqualsMatcher(table.getKeys()[0], key));
        }else {
            for(int i=0; i<keyFields.size(); i++) {

                Method fieldGetMethod = GenericMapper.getMethod(entityClass, keyFields.get(i), MethodType.GET);
                try {
                    String keyColumn = keyColumns.get(i);
                    Object keyValue = fieldGetMethod.invoke(key); //非基本类型主键通过反射调用get method获取键值
                    matcherList.add(new EqualsMatcher(keyColumn, keyValue));
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                } catch (InvocationTargetException e) {
                    log.error(e.getMessage(), e.getTargetException());
                }
            }

        }
        return matcherList;

    }


    private List<Matcher> getKeyConditionsFomEntity(ENTITY entity) {
        List<Matcher> matcherList = new ArrayList<Matcher>();

        if(CollectionUtils.isEmpty(keyColumns)) {
            throw new RuntimeException("no key columns found!");
        }

        for(int i=0; i<keyFields.size(); i++) {

            Method fieldGetMethod = GenericMapper.getMethod(entityClass, keyFields.get(i), MethodType.GET);
            try {
                String keyColumn = keyColumns.get(i);
                Object keyValue = fieldGetMethod.invoke(entity);
                matcherList.add(new EqualsMatcher(keyColumn, keyValue));
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e.getTargetException());
            }
        }

        return matcherList;

    }


    private Map<String, Object> getDataMapByColumns(List<String> columns, ENTITY entity) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for(String column : columns) {
            Object value = null;
            Field field = GenericMapper.getFieldWithColumn(entityClass, column);
            if(field == null) {
                throw new RuntimeException("column [" + column + "] has no appropriate field!");
            }

            Method getMethod = GenericMapper.getMethod(entityClass, field, MethodType.GET);
            if(getMethod == null) {
                throw new RuntimeException("field [" + field.getName() + "] has no get method!");
            }
            try {
                value = getMethod.invoke(entity);
            }  catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e.getTargetException());
            }
            if(value != null) {
                dataMap.put(column, value);
            }

        }
        return dataMap;
    }

    private ActionBuilder getBuilder(ActionMode actionMode) {
        ActionBuilder builder = ActionBuilderContainer.getActionBuilder(table, actionMode);
        if(null == builder) {
            if(ActionMode.QUERY.equals(actionMode)) {
                builder = new QueryBuilder(table, shardStrategy);
            }else if(ActionMode.INSERT.equals(actionMode)) {
                builder = new InsertBuilder(table, shardStrategy);
            }else if(ActionMode.DELETE.equals(actionMode)) {
                builder = new DeleteBuilder(table, shardStrategy);
            }else if(ActionMode.UPDATE.equals(actionMode)) {
                builder = new UpdateBuilder(table, shardStrategy);
            }else {
                throw new RuntimeException("Invalid action mode!");
            }
        }
        ActionBuilderContainer.setActionBuilder(builder);
        return builder;
    }


}
