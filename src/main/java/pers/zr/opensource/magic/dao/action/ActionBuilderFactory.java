package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 * Created by zhurong on 2016-5-17.
 */
public class ActionBuilderFactory {

    public static ActionBuilder getBuilder(ActionMode actionMode, ActionTable table) {
        ActionBuilder builder = ActionBuilderContainer.getActionBuilder(table, actionMode);
        if(null == builder) {
            if(ActionMode.QUERY.equals(actionMode)) {
                builder = new QueryBuilder(table);
            }else if(ActionMode.INSERT.equals(actionMode)) {
                builder = new InsertBuilder(table);
            }else if(ActionMode.DELETE.equals(actionMode)) {
                builder = new DeleteBuilder(table);
            }else if(ActionMode.UPDATE.equals(actionMode)) {
                builder = new UpdateBuilder(table);
            }else {
                throw new RuntimeException("Invalid action mode!");
            }
        }
        ActionBuilderContainer.setActionBuilder(builder);
        return builder;
    }


}
