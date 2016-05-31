package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 * Created by zhurong on 2016-4-28.
 */
public class InsertBuilder extends ActionBuilder {

    public InsertBuilder(ActionTable table) {
        this.table = table;
    }

    @Override
    public Insert build() {
        Insert insert = new Insert();
        insert.table = this.table;
        return insert;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.INSERT;
    }
}
