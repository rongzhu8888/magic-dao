package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 *
 * Created by zhurong on 2016-4-28.
 */
public class DeleteBuilder extends ActionBuilder {

    public DeleteBuilder(ActionTable table) {
        this.table = table;
    }


    @Override
    public Delete build() {
        Delete delete = new Delete();
        delete.table = this.table;
        return delete;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.DELETE;
    }
}
