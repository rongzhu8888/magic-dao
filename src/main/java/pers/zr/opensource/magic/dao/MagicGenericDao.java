package pers.zr.opensource.magic.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.action.*;
import pers.zr.opensource.magic.dao.annotation.Column;
import pers.zr.opensource.magic.dao.annotation.Key;
import pers.zr.opensource.magic.dao.annotation.TableShard;
import pers.zr.opensource.magic.dao.annotation.Table;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.mapper.GenericMapper;
import pers.zr.opensource.magic.dao.constants.MethodType;
import pers.zr.opensource.magic.dao.mapper.MapperContextHolder;
import pers.zr.opensource.magic.dao.matcher.EqualsMatcher;
import pers.zr.opensource.magic.dao.matcher.Matcher;
import pers.zr.opensource.magic.dao.order.Order;
import pers.zr.opensource.magic.dao.page.Page;
import pers.zr.opensource.magic.dao.shard.DefaultTableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;
import pers.zr.opensource.magic.dao.utils.ClassUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Common and abstract implements of MagicDao
 * </p>
 * Created by zhurong on 2016-4-29.
 */
public abstract class MagicGenericDao<KEY extends Serializable, ENTITY extends Serializable> implements MagicDao<KEY, ENTITY> {

    protected final Log log = LogFactory.getLog(getClass());

    /** magic data source*/
    protected MagicDataSource magicDataSource;

    /** table shard handler*/
    protected TableShardHandler tableShardHandler = new DefaultTableShardHandler();

    /** entity class */
    private Class<ENTITY> entityClass;

    /** table */
    private ActionTable table;

    /** key class */
    private Class<KEY> keyClass;

    /** key fields of entity*/
    private List<Field> keyFields;

    /** Generic RowMapper for set data */
    protected RowMapper<ENTITY> rowMapper;


    @SuppressWarnings("unchecked")
    public MagicGenericDao() {

        if(log.isInfoEnabled()) {
            log.info("initialize instance of " + getClass());
        }

        //Get entity class & key class
        Type[] types = ClassUtil.getGenericTypes(getClass());
        keyClass = (Class<KEY>)types[0];
        entityClass = (Class<ENTITY>)types[1];

        String tableName = null;


        //get table matched by entity (analysis @Table)
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        TableShard tableShardAnnotation = entityClass.getAnnotation(TableShard.class);

        if(tableShardAnnotation != null) {
            tableName = tableShardAnnotation.shardTable();
        }
        if(null == tableName && null != tableAnnotation) {
            tableName = tableAnnotation.name();
        }
        if(null == tableName) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] must be annotated with TableName!");
        }

        //get all fields of entity
        Set<Field> fields = ClassUtil.getAllFields(entityClass);
        if(CollectionUtils.isEmpty(fields)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no fields!");
        }

        //get all methods of entity
        Set<Method> methods = ClassUtil.getAllMethods(entityClass);
        if(CollectionUtils.isEmpty(methods)) {
            throw new RuntimeException("Class [" + entityClass.getName() +"] has no methods!");
        }

        //analysis @Key and @Column
        List<String> keyColumns = new ArrayList<String>();
        keyFields = new ArrayList<Field>();
        List<String> tableColumns = new ArrayList<String>();
        List<String> defaultInsertColumns = new ArrayList<String>();
        List<String> defaultUpdateColumns = new ArrayList<String>();

        for(Field field : fields) {

            Key keyAnnotation = field.getAnnotation(Key.class);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if(null != keyAnnotation) {
                keyColumns.add(keyAnnotation.column());
                keyFields.add(field);
                tableColumns.add(keyAnnotation.column());

                if(!keyAnnotation.autoIncrement()) {
                    defaultInsertColumns.add(keyAnnotation.column());
                }

                MapperContextHolder.setFieldWithColumn(entityClass, keyAnnotation.column(), field);

            }else if(columnAnnotation != null) {
                tableColumns.add(columnAnnotation.value());

                if(!columnAnnotation.readOnly()) {
                    defaultInsertColumns.add(columnAnnotation.value());
                }
                MapperContextHolder.setFieldWithColumn(entityClass, columnAnnotation.value(), field);
            }

            //get setter and getter method
            String fieldName = field.getName();
            for(Method method : methods) {
                String methodName = method.getName();
                if(methodName.equalsIgnoreCase("set" + fieldName) ||
                        (fieldName.startsWith("is") && methodName.equalsIgnoreCase("set" + fieldName.substring(2)))) {

                    MapperContextHolder.setMethod(entityClass, field, method, MethodType.SET);

                }else if(methodName.equalsIgnoreCase("get" + fieldName) ||
                        methodName.equalsIgnoreCase("is" + fieldName) ||
                        (fieldName.startsWith("is") && methodName.equalsIgnoreCase(fieldName))) {

                    MapperContextHolder.setMethod(entityClass, field, method, MethodType.GET);

                }

            }
        }


        //update columns which inside inserted columns but not key
        defaultUpdateColumns.addAll(defaultInsertColumns.stream().filter(column -> !keyColumns.contains(column)).collect(Collectors.toList()));

        table = new ActionTable();
        table.setTableName(tableAnnotation.name());
        table.setKeys(keyColumns);
        table.setColumns(tableColumns);
        table.setDefaultInsertColumns(defaultInsertColumns);
        table.setDefaultUpdateColumns(defaultUpdateColumns);

        rowMapper = new GenericMapper<ENTITY>(entityClass);
        MapperContextHolder.setRowMapper(entityClass, rowMapper);

        //get shard strategy
        if(null != tableShardAnnotation) {
            int shardCount = tableShardAnnotation.shardCount();
            String[] shardColumns = tableShardAnnotation.shardColumns();
            String separator = tableShardAnnotation.separator();
            table.setTableShardStrategy(new TableShardStrategy(tableName, shardCount, shardColumns, separator));
            table.setTableShardHandler(tableShardHandler);
        }

    }


    @Override
    public ENTITY get(KEY key) {

        Query query = ActionBuilderFactory.getBuilder(ActionMode.QUERY, table).build();
        query.setQueryFields(table.getColumns());
        query.addConditions(getKeyConditions(key));

        List<ENTITY> list = magicDataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);

    }

    @Override
    public void insert(ENTITY entity){
        Insert insert = ActionBuilderFactory.getBuilder(ActionMode.INSERT, table).build();
        insert.setInsertFields(getDataMapByColumns(table.getDefaultInsertColumns(), entity));
        magicDataSource.getJdbcTemplate(ActionMode.INSERT).update(insert.getSql(), insert.getParams());

    }


    @Override
    public Long insertForId(ENTITY entity){
        Insert insert = ActionBuilderFactory.getBuilder(ActionMode.INSERT, table).build();
        Map<String, Object> dataMap = getDataMapByColumns(table.getDefaultInsertColumns(), entity);
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
        JdbcTemplate jdbcTemplate = magicDataSource.getJdbcTemplate(ActionMode.INSERT);
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, columnsArray);
                    for(int k=1; k<=paramsArray.length; k++) {
                        ps.setObject(k, paramsArray[k-1]);
                    }
                    return ps;
                }, keyHolder);

        return keyHolder.getKey().longValue();

    }


    @Override
    public void update(ENTITY entity) {
        Update update = ActionBuilderFactory.getBuilder(ActionMode.UPDATE, table).build();
        update.addConditions(getKeyConditionsFromEntity(entity));
        update.setUpdateFields(getDataMapByColumns(table.getDefaultUpdateColumns(), entity));
        magicDataSource.getJdbcTemplate(ActionMode.UPDATE).update(update.getSql(), update.getParams());

    }


    @Override
    public void delete(KEY key){
        Delete delete = ActionBuilderFactory.getBuilder(ActionMode.DELETE, table).build();
        delete.addConditions(getKeyConditions(key));
        magicDataSource.getJdbcTemplate(ActionMode.DELETE).update(delete.getSql(), delete.getParams());

    }

    @Override
    public void delete(Matcher...conditions) {
        Delete delete = ActionBuilderFactory.getBuilder(ActionMode.DELETE, table).build();
        if(null != conditions && conditions.length >0) {
            delete.addConditions(Arrays.asList(conditions));
        }
        magicDataSource.getJdbcTemplate(ActionMode.DELETE).update(delete.getSql(), delete.getParams());

    }

    @Override
    public void update(Map<String, Object> valueMap, Matcher...conditions) {
        Update update = ActionBuilderFactory.getBuilder(ActionMode.UPDATE, table).build();
        if(null != conditions && conditions.length >0) {
            update.addConditions(Arrays.asList(conditions));
        }
        update.setUpdateFields(valueMap);
        magicDataSource.getJdbcTemplate(ActionMode.UPDATE).update(update.getSql(), update.getParams());

    }

    @Override
    public List<ENTITY> query(Matcher...conditions) {
        Query query = ActionBuilderFactory.getBuilder(ActionMode.QUERY, table).build();
        query.setQueryFields(table.getColumns());
        query.addConditions(Arrays.asList(conditions));
        List<ENTITY> list = magicDataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<ENTITY>() : list;

    }

    @Override
    public List<ENTITY> query(Page page, Matcher...conditions) {
        return query(page, null, conditions);

    }

    @Override
    public List<ENTITY> query(List<Order> orders, Matcher...conditions) {
        return query(null, orders, conditions);

    }

    @Override
    public List<ENTITY> query(Page page, List<Order> orders, Matcher...conditions){
        Query query = ActionBuilderFactory.getBuilder(ActionMode.QUERY, table).build();
        query.setQueryFields(table.getColumns());
        query.addConditions(Arrays.asList(conditions));
        query.setOrders(orders);
        query.setPage(page);
        List<ENTITY> list = magicDataSource.getJdbcTemplate(ActionMode.QUERY).query(query.getSql(), query.getParams(), rowMapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<ENTITY>() : list;

    }

    @Override
    public Long getCount(Matcher...conditions) {
        Query query = ActionBuilderFactory.getBuilder(ActionMode.QUERY, table).build();
        query.setQueryFields(Collections.singletonList("COUNT(1)"));
        query.addConditions(Arrays.asList(conditions));
        return magicDataSource.getJdbcTemplate(ActionMode.QUERY).queryForObject(query.getSql(), query.getParams(), Long.class);

    }

    private List<Matcher> getKeyConditions(KEY key) {
        List<Matcher> matcherList = new ArrayList<Matcher>();

        List<String> keyColumns = table.getKeys();
        if(CollectionUtils.isEmpty(table.getKeys())) {
            throw new RuntimeException("no key columns found!");
        }

        if(table.getKeys().size() == 1 && ClassUtil.isBasicType(keyClass)) {
            matcherList.add(new EqualsMatcher(keyColumns.get(0), key));
        }else {
            for(int i=0; i<keyFields.size(); i++) {

                Method fieldGetMethod = MapperContextHolder.getMethod(entityClass, keyFields.get(i), MethodType.GET);
                try {
                    String keyColumn = keyColumns.get(i);
                    Object keyValue = fieldGetMethod.invoke(key);
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


    private List<Matcher> getKeyConditionsFromEntity(ENTITY entity) {
        List<Matcher> matcherList = new ArrayList<Matcher>();

        List<String> keyColumns = table.getKeys();
        if(CollectionUtils.isEmpty(keyColumns)) {
            throw new RuntimeException("no key columns found!");
        }

        for(int i=0; i<keyFields.size(); i++) {

            Method fieldGetMethod = MapperContextHolder.getMethod(entityClass, keyFields.get(i), MethodType.GET);
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
            Field field = MapperContextHolder.getFieldWithColumn(entityClass, column);
            if(field == null) {
                throw new RuntimeException("column [" + column + "] has no appropriate field!");
            }

            Method getMethod = MapperContextHolder.getMethod(entityClass, field, MethodType.GET);
            if(getMethod == null) {
                throw new RuntimeException("field [" + field.getName() + "] has no getMethod!");
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

    public void setMagicDataSource(MagicDataSource magicDataSource) {
        this.magicDataSource = magicDataSource;
    }

    public void setTableShardHandler(TableShardHandler tableShardHandler) {
        this.tableShardHandler = tableShardHandler;
        table.setTableShardHandler(this.tableShardHandler);

    }
}
