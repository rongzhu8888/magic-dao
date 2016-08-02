package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

import java.util.List;
import java.util.Objects;

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
        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(keys, that.keys) &&
                Objects.equals(columns, that.columns) &&
                Objects.equals(tableShardStrategy, that.tableShardStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, keys, columns, tableShardStrategy);
    }
}
