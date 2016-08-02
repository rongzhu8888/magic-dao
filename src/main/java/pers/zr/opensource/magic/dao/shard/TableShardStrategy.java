package pers.zr.opensource.magic.dao.shard;

/**
 *
 * TableShard Strategy
 *
 * Created by zhurong on 2016-4-28.
 */
public class TableShardStrategy {

    private String shardTable;

    private int shardCount;

    private String[] shardColumns;

    private String separator;

    public TableShardStrategy(String shardTable, int shardCount, String[] shardColumns, String separator) {
        this.shardTable = shardTable;
        this.shardCount = shardCount;
        this.shardColumns = shardColumns;
        this.separator = separator;
    }

    public String getShardTable() {
        return shardTable;
    }

    public int getShardCount() {
        return shardCount;
    }

    public String[] getShardColumns() {
        return shardColumns;
    }

    public String getSeparator() {
        return separator;
    }
}
