package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class ActionTable {

    /** table name*/
    private String tableName;

    /** keys*/
    private List<String> keys;

    private List<String> columns;

    private List<String> defaultInsertColumns;

    private List<String> defaultUpdateColumns;

    private TableShardStrategy tableShardStrategy;

    private TableShardHandler tableShardHandler;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public TableShardStrategy getTableShardStrategy() {
        return tableShardStrategy;
    }

    public void setTableShardStrategy(TableShardStrategy tableShardStrategy) {
        this.tableShardStrategy = tableShardStrategy;
    }

    public TableShardHandler getTableShardHandler() {
        return tableShardHandler;
    }

    public void setTableShardHandler(TableShardHandler tableShardHandler) {
        this.tableShardHandler = tableShardHandler;
    }

    public List<String> getDefaultInsertColumns() {
        return defaultInsertColumns;
    }

    public void setDefaultInsertColumns(List<String> defaultInsertColumns) {
        this.defaultInsertColumns = defaultInsertColumns;
    }

    public List<String> getDefaultUpdateColumns() {
        return defaultUpdateColumns;
    }

    public void setDefaultUpdateColumns(List<String> defaultUpdateColumns) {
        this.defaultUpdateColumns = defaultUpdateColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionTable that = (ActionTable) o;

        if (!tableName.equals(that.tableName)) return false;
        if (!keys.equals(that.keys)) return false;
        if (!columns.equals(that.columns)) return false;
        return tableShardStrategy.equals(that.tableShardStrategy);

    }

    @Override
    public int hashCode() {
        int result = tableName.hashCode();
        result = 31 * result + keys.hashCode();
        result = 31 * result + columns.hashCode();
        result = 31 * result + tableShardStrategy.hashCode();
        return result;
    }
}
