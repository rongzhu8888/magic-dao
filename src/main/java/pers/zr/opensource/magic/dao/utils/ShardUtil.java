package pers.zr.opensource.magic.dao.utils;

/**
 * Created by zhurong on 2016-5-5.
 */
public class ShardUtil {

    public static String getShardTableName(String tableBasicName, String separator, int shardCount, String shardData) {
        int tableIndex = HashSlotUtil.getSlot(shardData, shardCount);
        return tableBasicName + separator + ((tableIndex < 10) ? "0" + tableIndex : String.valueOf(tableIndex));
    }
}
