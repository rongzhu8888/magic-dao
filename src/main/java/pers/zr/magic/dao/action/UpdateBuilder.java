package pers.zr.magic.dao.action;

import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.shard.ShardStrategy;

/**
 * Created by zhurong on 2016-4-28.
 */
public class UpdateBuilder extends ActionBuilder {

    public UpdateBuilder(ActionTable table) {
        this.table = table;
    }

    public UpdateBuilder(ActionTable table, ShardStrategy strategy) {
        this.table = table;
        this.shardStrategy = strategy;
    }

    @Override
    public Update build() {

        Update update = new Update();
        update.table = this.table;
        update.shardStrategy = this.shardStrategy;
        return update;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.UPDATE;
    }
}
