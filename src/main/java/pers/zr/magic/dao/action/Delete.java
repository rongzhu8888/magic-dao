package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.zr.magic.dao.constants.ActionMode;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Delete extends ConditionAction {

    private Logger log = LogManager.getLogger(Delete.class);

    @Override
    public String getSql() {
        if(null == this.sql) {
            this.sql = (new StringBuilder()).append("DELETE FROM ").append(table.getTableName())
                    .append(" ").append(getConSql()).toString();
            if (log.isDebugEnabled()) {
                log.debug("Delete SQL: ###" + sql + "###");
            }
        }
        return this.sql;
    }

    @Override
    public Object[] getParams() {
        this.params = getConParams().toArray();
        return this.params;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.DELETE;
    }


}
