package pers.zr.opensource.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.utils.ShardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Insert extends Action {

    private Map<String, Object> insertFields;

    public void setInsertFields(Map<String, Object> insertFields) {
        this.insertFields = insertFields;
    }

    private String shardTableName = null;

    @Override
    public String getSql() {

        if(null == this.sql) {
            parse();
        }
        if (log.isDebugEnabled()) {
            log.debug("### [ " + sql + "] ###");
        }
        return this.sql;
    }

    @Override
    public Object[] getParams() {

        if(null == this.params) {
            parse();
        }
        return this.params;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.INSERT;
    }


    private void parse() {

        if(CollectionUtils.isEmpty(insertFields)) {
            throw new RuntimeException("insert fields can not be empty!");
        }

        List<Object> paramsList = new ArrayList<Object>(insertFields.size());
        StringBuilder sqlBuilder = new StringBuilder(" (");


        for(String column : insertFields.keySet()) {
            sqlBuilder.append(column).append(",");
            Object value = insertFields.get(column);
            paramsList.add(value);
            if(this.shardStrategy != null && shardStrategy.getShardColumn().equalsIgnoreCase(column)) {
                this.shardTableName = ShardUtil.getShardTableName(table.getTableName(),
                        shardStrategy.getSeparator(),
                        shardStrategy.getShardCount(),
                        String.valueOf(value));
            }
        }

        if(shardStrategy != null && this.shardTableName == null) {
            throw new RuntimeException("Shard error: can not find shard column from insert data!");
        }

        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(") VALUES (");
        for(int i=0; i<insertFields.size(); i++) {
            sqlBuilder.append("?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(")");

        String tableName = this.table.getTableName();
        if(null != this.shardStrategy) {
            tableName = this.shardTableName;
        }
        this.sql = "INSERT INTO " + tableName + sqlBuilder.toString();
        this.params = paramsList.toArray();


    }

}
