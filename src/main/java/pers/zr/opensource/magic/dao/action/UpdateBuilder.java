package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 * Created by zhurong on 2016-4-28.
 */
public class UpdateBuilder extends ActionBuilder {

    public UpdateBuilder(ActionTable table) {
        this.table = table;
    }


    @Override
    public Update build() {
        Update update = new Update();
        update.table = this.table;
        return update;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.UPDATE;
    }
}
