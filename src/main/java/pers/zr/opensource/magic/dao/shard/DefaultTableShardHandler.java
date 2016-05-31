package pers.zr.opensource.magic.dao.shard;

import pers.zr.opensource.magic.dao.utils.HashSlotUtil;

/**
 * Created by zhurong on 2016-5-31.
 */
public class DefaultTableShardHandler implements TableShardHandler {

    @Override
    public String getShardTableName(String tableBasicName, int shardCount, String separator, Object columnValue) {
        if(columnValue == null) {
            throw new RuntimeException("Failed to get shard table name, caused by columnValue is null");
        }
        int tableIndex = HashSlotUtil.getSlot(String.valueOf(columnValue), shardCount);
        return tableBasicName + separator + ((tableIndex < 10) ? "0" + tableIndex : String.valueOf(tableIndex));
    }
}
