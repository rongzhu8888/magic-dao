package pers.zr.opensource.magic.dao.action;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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

        return new EqualsBuilder()
                .append(tableName, that.tableName)
                .append(keys, that.keys)
                .append(columns, that.columns)
                .append(tableShardStrategy, that.tableShardStrategy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tableName)
                .append(keys)
                .append(columns)
                .append(tableShardStrategy)
                .toHashCode();
    }
}
