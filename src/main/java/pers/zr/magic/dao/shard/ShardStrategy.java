package pers.zr.magic.dao.shard;

/**
 *
 * 分表策略
 *
 * Created by zhurong on 2016-4-28.
 */
public class ShardStrategy {

    private int shardingCount; //分表（片）数量

    private String shardingColumn; //sharding字段

    private String separator; //连接符

    public ShardStrategy(int shardingCount, String shardingColumn, String separator) {
        this.shardingCount = shardingCount;
        this.shardingColumn = shardingColumn;
        this.separator = separator;
    }

    public int getShardingCount() {
        return shardingCount;
    }

    public String getShardingColumn() {
        return shardingColumn;
    }

    public String getSeparator() {
        return separator;
    }
}
