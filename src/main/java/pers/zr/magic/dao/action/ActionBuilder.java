package pers.zr.magic.dao.action;

import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.feature.ShardStrategy;
import pers.zr.magic.dao.runtime.ActionTable;

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
