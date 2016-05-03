package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.matcher.EqualsMatcher;
import pers.zr.magic.dao.core.runtime.ActionCondition;

import java.util.Collection;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Update extends  ConditionAction {

    private Map<String, Object> updateFields;

    private String sql;

    private Object[] params;

    public void setUpdateFields(Map<String, Object> updateFields) {
        this.updateFields = updateFields;
    }

    @Override
    public String getSql() {
        return null;
    }

    @Override
    public Object[] getParams() {
        return new Object[0];
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.UPDATE;
    }
}
