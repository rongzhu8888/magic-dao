package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.ShardStrategy;

/**
 *
 * Created by zhurong on 2016-4-28.
 */
public abstract class ActionBuilder {

    protected ActionTable table;

    protected ShardStrategy shardStrategy;

    public abstract <T extends Action> T build();

    public ActionTable getTable() {
        return this.table;
    }

    public abstract ActionMode getActionMode();


}
