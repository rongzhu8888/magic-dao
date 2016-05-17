package pers.zr.magic.dao.utils;

/**
 * Created by zhurong on 2016-5-5.
 */
public class ShardingUtil {

    public static String getShardTableSuffix(String value, int shardCount) {
        int shard = HashSlotUtil.getSlot(value, shardCount);
        return (shard < 10) ? "0" + shard : String.valueOf(shard);
    }
}
