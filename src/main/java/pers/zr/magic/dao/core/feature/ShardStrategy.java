package pers.zr.magic.dao.core.feature;

/**
 *
 * 分表策略
 *
 * Created by zhurong on 2016-4-28.
 */
public class ShardStrategy {

    private int shardCount; //分表（片）数量

    private String shardColumn; //sharding依赖字段

    private String connectSymbol;

    public ShardStrategy(int shardCount, String shardColumn) {
        this.shardCount = shardCount;
        this.shardColumn = shardColumn;
        this.connectSymbol = "_";
    }

    public ShardStrategy(int shardCount, String shardColumn, String connectSymbol) {
        this.shardCount = shardCount;
        this.shardColumn = shardColumn;
        this.connectSymbol = connectSymbol;
    }

    public int getShardCount() {
        return shardCount;
    }

    public void setShardCount(int shardCount) {
        this.shardCount = shardCount;
    }

    public String getShardColumn() {
        return shardColumn;
    }

    public void setShardColumn(String shardColumn) {
        this.shardColumn = shardColumn;
    }

    public String getConnectSymbol() {
        return connectSymbol;
    }

    public void setConnectSymbol(String connectSymbol) {
        this.connectSymbol = connectSymbol;
    }


}
