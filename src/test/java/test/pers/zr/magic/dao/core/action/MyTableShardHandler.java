package test.pers.zr.magic.dao.core.action;

import pers.zr.opensource.magic.dao.shard.TableShardHandler;

/**
 * Created by zhurong on 2016-5-31.
 */
public class MyTableShardHandler implements TableShardHandler {
    @Override
    public String getShardTableName(String tableBasicName, int shardCount, String separator, Object columnValue) {
        return "table_xx";
    }
}
