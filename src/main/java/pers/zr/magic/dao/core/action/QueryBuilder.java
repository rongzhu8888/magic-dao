package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;

/**
 * Created by zhurong on 2016-4-28.
 */
public class QueryBuilder extends ActionBuilder {
    @Override
    public Query build() {

        Query query = new Query();
        query.setTable(table);
        query.setShardStrategy(shardStrategy);
        return query;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.QUERY;
    }
}
