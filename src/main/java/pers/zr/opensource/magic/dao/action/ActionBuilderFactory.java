package pers.zr.opensource.magic.dao.action;

import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.ShardStrategy;

/**
 * Created by zhurong on 2016-5-17.
 */
public class ActionBuilderFactory {

    public static ActionBuilder getBuilder(ActionMode actionMode, ActionTable table) {

        return getBuilder(actionMode, table, null);
    }

    public static ActionBuilder getBuilder(ActionMode actionMode, ActionTable table, ShardStrategy shardStrategy) {
        ActionBuilder builder = ActionBuilderContainer.getActionBuilder(table, actionMode);
        if(null == builder) {
            if(ActionMode.QUERY.equals(actionMode)) {
                builder = new QueryBuilder(table, shardStrategy);
            }else if(ActionMode.INSERT.equals(actionMode)) {
                builder = new InsertBuilder(table, shardStrategy);
            }else if(ActionMode.DELETE.equals(actionMode)) {
                builder = new DeleteBuilder(table, shardStrategy);
            }else if(ActionMode.UPDATE.equals(actionMode)) {
                builder = new UpdateBuilder(table, shardStrategy);
            }else {
                throw new RuntimeException("Invalid action mode!");
            }
        }
        ActionBuilderContainer.setActionBuilder(builder);
        return builder;
    }


}
