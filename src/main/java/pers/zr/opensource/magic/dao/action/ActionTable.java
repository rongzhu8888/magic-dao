package pers.zr.opensource.magic.dao.action;

/**
 * Created by zhurong on 2016-4-28.
 */
public class ActionTable {

    /** table name*/
    private String tableName;

    /** keys*/
    private String[] keys;

    /** columns*/
    private String[] columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ActionTable that = (ActionTable) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(tableName, that.tableName)
                .append(keys, that.keys)
                .append(columns, that.columns)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(tableName)
                .append(keys)
                .append(columns)
                .toHashCode();
    }
}
