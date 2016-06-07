package pers.zr.opensource.magic.dao.shard;

import pers.zr.opensource.magic.dao.utils.HashSlotUtil;

/**
 * Created by zhurong on 2016-5-31.
 */
public class DefaultTableShardHandler implements TableShardHandler {

    @Override
    public String getActualTableName(TableShardStrategy shardStrategy, Object columnValue) {
        if(shardStrategy == null) {
            throw new RuntimeException("Failed to get actual table name, caused by tableShardStrategy is null!");
        }
        if(columnValue == null) {
            throw new RuntimeException("Failed to get actual table name, caused by columnValue is null!");
        }
        int tableIndex = HashSlotUtil.getSlot(String.valueOf(columnValue), shardStrategy.getShardCount());
        return shardStrategy.getShardTable() + shardStrategy.getSeparator() + ((tableIndex < 10) ? "0" + tableIndex : String.valueOf(tableIndex));
    }
}
