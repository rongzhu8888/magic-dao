package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.utils.ShardingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Insert extends Action {

    private static Logger log = LogManager.getLogger(Insert.class);

    private Map<String, Object> insertFields;

    public void setInsertFields(Map<String, Object> insertFields) {
        this.insertFields = insertFields;
    }

    private String shardingTableName = null;

    @Override
    public String getSql() {

        if(null == this.sql) {
            parse();
            if (log.isDebugEnabled()) {
                log.debug("Insert SQL: ###" + sql + "###");
            }
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
            if(this.shardStrategy != null && shardStrategy.getShardingColumn().equalsIgnoreCase(column)) {

                this.shardingTableName = table.getTableName() + shardStrategy.getConnector()
                        + ShardingUtil.getShardingTableSuffix(String.valueOf(value), shardStrategy.getShardingCount());

            }
        }

        if(shardStrategy != null && this.shardingTableName == null) {
            throw new RuntimeException("Shard error: can not find shard column from conditions!");
        }

        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(") VALUES (");
        for(int i=0; i<insertFields.size(); i++) {
            sqlBuilder.append("?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(")");

        String tableName = this.table.getTableName();
        if(null != this.shardStrategy) {
            tableName = this.shardingTableName;
        }
        this.sql = "INSERT INTO " + tableName + sqlBuilder.toString();
        this.params = paramsList.toArray();


    }

}
