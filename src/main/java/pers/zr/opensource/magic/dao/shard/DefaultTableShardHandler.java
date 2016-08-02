package pers.zr.opensource.magic.dao.shard;

import pers.zr.opensource.magic.dao.utils.HashSlotUtil;

/**
 * Created by zhurong on 2016-5-31.
 */
public class DefaultTableShardHandler implements TableShardHandler {

    @Override
    public String getRealTableName(TableShardStrategy shardStrategy, Object... columnValues) {
        if(shardStrategy == null) {
            throw new RuntimeException("Failed to get actual table name, caused by tableShardStrategy is null!");
        }
        if(columnValues == null || columnValues.length < 1) {
            throw new RuntimeException("Failed to get actual table name, caused by columnValue is empty or null!");
        }

        StringBuilder sb = new StringBuilder();
        for(Object o : columnValues) {
            sb.append(String.valueOf(o));
        }
        int tableIndex = HashSlotUtil.getSlot(sb.toString(), shardStrategy.getShardCount());
        return shardStrategy.getShardTable() + shardStrategy.getSeparator() + ((tableIndex < 10) ? "0" + tableIndex : String.valueOf(tableIndex));
    }
}
