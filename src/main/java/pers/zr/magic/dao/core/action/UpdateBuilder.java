package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;

/**
 * Created by zhurong on 2016-4-28.
 */
public class UpdateBuilder extends ActionBuilder {
    @Override
    public Update build() {

        Update update = new Update();
        update.setTable(table);
        update.setShardStrategy(shardStrategy);
        return update;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.UPDATE;
    }
}
