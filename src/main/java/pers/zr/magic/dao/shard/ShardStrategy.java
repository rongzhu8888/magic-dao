package pers.zr.magic.dao.shard;

/**
 *
 * 分表策略
 *
 * Created by zhurong on 2016-4-28.
 */
public class ShardStrategy {

    private int shardCount; //分表（片）数量

    private String shardColumn; //shard字段

    private String separator; //连接符

    public ShardStrategy(int shardCount, String shardColumn, String separator) {
        this.shardCount = shardCount;
        this.shardColumn = shardColumn;
        this.separator = separator;
    }

    public int getShardCount() {
        return shardCount;
    }

    public String getShardColumn() {
        return shardColumn;
    }

    public String getSeparator() {
        return separator;
    }
}
