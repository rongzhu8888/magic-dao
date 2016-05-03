package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.feature.ShardStrategy;
import pers.zr.magic.dao.core.runtime.ActionTable;

/**
 *
 * Created by zhurong on 2016-4-28.
 */
public abstract class ActionBuilder {

    protected ActionTable table;

    protected ShardStrategy shardStrategy;

    public ActionBuilder setTable(ActionTable table) {
        this.table = table;
        return this;
    }

    public ActionBuilder setShardStrategy(ShardStrategy shardStrategy) {
        this.shardStrategy = shardStrategy;
        return this;
    }

    public abstract <T extends Action> T build();

    public ActionTable getTable() {
        return this.table;
    }

    public abstract ActionMode getActionMode();


}
