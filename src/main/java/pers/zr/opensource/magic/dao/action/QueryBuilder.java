package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.ShardStrategy;

/**
 * Created by zhurong on 2016-4-28.
 */
public class QueryBuilder extends ActionBuilder {

    public QueryBuilder(ActionTable table) {
        this.table = table;
    }

    public QueryBuilder(ActionTable table, ShardStrategy strategy) {
        this.table = table;
        this.shardStrategy = strategy;
    }

    @Override
    public Query build() {

        Query query = new Query();
        query.table = this.table;
        query.shardStrategy = this.shardStrategy;
        return query;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.QUERY;
    }
}
