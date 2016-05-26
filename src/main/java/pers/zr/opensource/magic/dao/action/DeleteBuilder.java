package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.ShardStrategy;

/**
 *
 * Created by zhurong on 2016-4-28.
 */
public class DeleteBuilder extends ActionBuilder {

    public DeleteBuilder(ActionTable table) {
        this.table = table;
    }

    public DeleteBuilder(ActionTable table, ShardStrategy strategy) {
        this.table = table;
        this.shardStrategy = strategy;
    }

    @Override
    public Delete build() {
        Delete delete = new Delete();
        delete.table = this.table;
        delete.shardStrategy = this.shardStrategy;
        return delete;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.DELETE;
    }
}
