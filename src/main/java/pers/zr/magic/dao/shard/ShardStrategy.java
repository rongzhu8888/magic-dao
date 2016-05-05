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

    private String connector; //连接符

    public ShardStrategy(int shardingCount, String shardingColumn) {
        this.shardingCount = shardingCount;
        this.shardingColumn = shardingColumn;
    }

    public ShardStrategy(int shardingCount, String shardingColumn, String connector) {
        this.shardingCount = shardingCount;
        this.shardingColumn = shardingColumn;
        this.connector = connector;
    }

    public int getShardingCount() {
        return shardingCount;
    }

    public String getShardingColumn() {
        return shardingColumn;
    }

    public String getConnector() {
        return connector;
    }
}
