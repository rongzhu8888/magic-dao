package pers.zr.magic.dao.core.matcher;

import pers.zr.magic.dao.core.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class InMatcher extends Matcher {

    private Object[] values;

    public InMatcher(String column, Object[] values) {
        this.column = column;
        this.values = values;
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.IN;
    }

    @Override
    public Object[] getValues() {
        return this.values;
    }


}
