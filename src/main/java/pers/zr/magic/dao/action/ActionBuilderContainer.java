package pers.zr.magic.dao.action;

import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.runtime.ActionTable;

import java.util.HashMap;
import java.util.Map;

/**
 *  ActionBuilder容器
 *
 *  缓存ActionBuilder，同一张表的多次同类型操作（如查询）共用一个ActionBuilder,减少对象创建数量
 *
 * 可以考虑在系统启动时扫描所有特定注解，初始化所有的ActionBuilder对象并填充到该容器中
 *
 * Created by zhurong on 2016-4-29.
 */
public class ActionBuilderContainer {

    private static Map<ActionTable, QueryBuilder> queryBuilderMap = new HashMap<ActionTable, QueryBuilder>();

    private static Map<ActionTable, DeleteBuilder> deleteBuilderMap = new HashMap<ActionTable, DeleteBuilder>();

    private static Map<ActionTable, InsertBuilder> insertBuilderMap = new HashMap<ActionTable, InsertBuilder>();

    private static Map<ActionTable, UpdateBuilder> updateBuilderMap = new HashMap<ActionTable, UpdateBuilder>();

    public static <T extends ActionBuilder> T getActionBuilder(ActionTable table, ActionMode mode) {

        if(table == null) {
            throw new IllegalArgumentException("ActionTable can not be null!");
        }

        ActionBuilder actionBuilder = null;

        if(ActionMode.QUERY == mode) {

            actionBuilder = queryBuilderMap.get(table);

        }else if(ActionMode.DELETE == mode) {

            actionBuilder = deleteBuilderMap.get(table);

        }else if(ActionMode.INSERT == mode) {

            actionBuilder = insertBuilderMap.get(table);

        }else if(ActionMode.UPDATE == mode) {

            actionBuilder = updateBuilderMap.get(table);

        }else {
            throw new IllegalArgumentException("Invalid action mode!");
        }

        return (T)actionBuilder;

    }

    public static void setActionBuilder(ActionBuilder builder) {


        if(builder == null) {
            throw new IllegalArgumentException("instance of ActionBuilder can not be null!");
        }

        ActionTable table = builder.getTable();

        if(table == null) {
            throw new IllegalArgumentException("ActionTable property of ActionBuilder can not be null!");
        }

        ActionMode mode = builder.getActionMode();

        if(ActionMode.QUERY == mode) {

            queryBuilderMap.put(table, (QueryBuilder) builder);

        }else if(ActionMode.INSERT == mode) {

            insertBuilderMap.put(table, (InsertBuilder) builder);

        }else if(ActionMode.UPDATE == mode){

            updateBuilderMap.put(table, (UpdateBuilder) builder);

        }else if(ActionMode.DELETE == mode) {

            deleteBuilderMap.put(table, (DeleteBuilder) builder);

        }else {

            throw new IllegalArgumentException("Invalid action mode!");

        }


    }


}
