package pers.zr.opensource.magic.dao.shard;

/**
 *
 * TableShard Strategy
 *
 * Created by zhurong on 2016-4-28.
 */
public class TableShardStrategy {

    private int shardCount;

    private String shardColumn;

    private String separator;

    public TableShardStrategy(int shardCount, String shardColumn, String separator) {
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
