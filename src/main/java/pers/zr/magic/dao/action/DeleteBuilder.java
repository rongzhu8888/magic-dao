package pers.zr.magic.dao.action;

import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.feature.ShardStrategy;
import pers.zr.magic.dao.runtime.ActionTable;

/**
 *
 * 同一张表的多次delete可以公用一个DeleteBuilder，所以需要在内存中缓存该对象
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
