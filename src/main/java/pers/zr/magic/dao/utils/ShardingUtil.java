package pers.zr.magic.dao.utils;

/**
 * Created by zhurong on 2016-5-5.
 */
public class ShardingUtil {

    public static String getShardingTableSuffix(String value, int shardingCount) {
        int shard = HashSlotUtil.getSlot(value, shardingCount);
        return (shard < 10) ? "0" + shard : String.valueOf(shard);
    }
}
