package pers.zr.opensource.magic.dao.shard;

/**
 * Created by zhurong on 2016-5-31.
 */
public interface TableShardHandler {

    public String getShardTableName(String tableBasicName,int shardCount, String separator, Object columnValue);


}
