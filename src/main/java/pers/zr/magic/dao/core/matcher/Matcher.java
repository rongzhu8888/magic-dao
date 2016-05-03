package pers.zr.magic.dao.core.matcher;

import pers.zr.magic.dao.core.constants.MatchType;

/**
 * Created by zhurong on 2016-4-28.
 */
public abstract class Matcher {

    protected String column;

    public abstract MatchType getMatchType();

    public abstract Object[] getValues();

    public String getColumn() {
        return column;
    }

}
