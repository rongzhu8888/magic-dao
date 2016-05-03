package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;

/**
 *
 * 同一张表的多次delete可以公用一个DeleteBuilder，所以需要在内存中缓存该对象
 * Created by zhurong on 2016-4-28.
 */
public class DeleteBuilder extends ActionBuilder {

    @Override
    public Delete build() {
        Delete delete = new Delete();
        delete.setTable(table);
        delete.setShardStrategy(shardStrategy);
        return delete;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.DELETE;
    }
}
