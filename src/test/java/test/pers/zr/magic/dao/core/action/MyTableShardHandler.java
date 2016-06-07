package test.pers.zr.magic.dao.core.action;

import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 * Created by zhurong on 2016-5-31.
 */
public class MyTableShardHandler implements TableShardHandler {
    @Override
    public String getActualTableName(TableShardStrategy shardStrategy, Object columnValue) {
        return "table_xx";
    }
}
