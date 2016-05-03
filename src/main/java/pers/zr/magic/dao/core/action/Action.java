package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.feature.ShardStrategy;
import pers.zr.magic.dao.core.runtime.ActionCondition;
import pers.zr.magic.dao.core.runtime.ActionTable;

import java.util.Collection;

/**
 * Created by zhurong on 2016-4-28.
 */
public abstract class Action {

    private ActionTable table;

    private ShardStrategy shardStrategy;

    public abstract String getSql();

    public abstract Object[] getParams();

    public abstract ActionMode getActionMode();

    public ActionTable getTable() {
        return table;
    }

    public void setTable(ActionTable table) {
        this.table = table;
    }

    public ShardStrategy getShardStrategy() {
        return shardStrategy;
    }

    public void setShardStrategy(ShardStrategy shardStrategy) {
        this.shardStrategy = shardStrategy;
    }
}
