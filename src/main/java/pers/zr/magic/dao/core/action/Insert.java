package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.runtime.ActionCondition;
import pers.zr.magic.dao.core.runtime.ActionValuePair;

import java.util.Collection;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Insert extends Action {

    private Collection<ActionValuePair> valuePairs;

    public void setValuePairs(Collection<ActionValuePair> valuePairs) {

        this.valuePairs = valuePairs;
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
        return ActionMode.INSERT;
    }
}
