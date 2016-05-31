package pers.zr.opensource.magic.dao.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

/**
 * Created by zhurong on 2016-4-28.
 */
public abstract class Action {

    protected Log log = LogFactory.getLog(getClass());

    protected ActionTable table;

    protected String sql;
    protected Object[] params;

    public abstract String getSql();

    public abstract Object[] getParams();

    public abstract ActionMode getActionMode();

}
