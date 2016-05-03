package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;

/**
 * Created by zhurong on 2016-4-28.
 */
public class InsertBuilder extends ActionBuilder {
    @Override
    public Insert build() {

        Insert insert = new Insert();
        insert.setTable(table);
        insert.setShardStrategy(shardStrategy);
        return insert;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.INSERT;
    }
}
