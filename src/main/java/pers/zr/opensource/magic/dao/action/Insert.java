package pers.zr.opensource.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.constants.MatchType;
import pers.zr.opensource.magic.dao.matcher.Matcher;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String realTableName = null;

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

    protected String getRealTableName() {
        if(null == realTableName) {
            //get actual table name when shard exist
            TableShardStrategy tableShardStrategy = table.getTableShardStrategy();
            TableShardHandler tableShardHandler = table.getTableShardHandler();
            if(null != tableShardHandler && null != tableShardStrategy) {
                String[] shardColumns = tableShardStrategy.getShardColumns();
                List<String> shardColumnList = Arrays.asList(shardColumns);
                List<Object> shardColumnValueList = new ArrayList<Object>();

                for(String column : insertFields.keySet()) {
                    if(shardColumnList.contains(column)) {
                        shardColumnValueList.add(insertFields.get(column));
                    }
                }
                realTableName = tableShardHandler.getRealTableName(tableShardStrategy,shardColumnValueList.toArray());
                if(tableShardStrategy != null && this.realTableName == null) {
                    throw new RuntimeException("Failed to get real name of shard table!");
                }

            }else {
                realTableName = table.getTableName();
            }
        }
        return realTableName;

    }

    private void parse() {

        if(CollectionUtils.isEmpty(insertFields)) {
            throw new RuntimeException("insert fields can not be empty!");
        }

        List<Object> paramsList = new ArrayList<Object>(insertFields.size());
        StringBuilder sqlBuilder = new StringBuilder(" (");

        TableShardStrategy tableShardStrategy = table.getTableShardStrategy();
        TableShardHandler tableShardHandler = table.getTableShardHandler();
        

        for(String column : insertFields.keySet()) {
            sqlBuilder.append(column).append(",");
            Object value = insertFields.get(column);
            paramsList.add(value);

        }

        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(") VALUES (");
        for(int i=0; i<insertFields.size(); i++) {
            sqlBuilder.append("?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(")");

        this.sql = "INSERT INTO " + getRealTableName() + sqlBuilder.toString();
        this.params = paramsList.toArray();


    }

}
