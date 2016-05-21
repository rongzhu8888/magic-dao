package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class InMatcher extends Matcher {

    private Object[] values;

    public InMatcher(String column, Object[] values) {

        if(null == values || values.length < 1) {
            throw new RuntimeException("InMatcher must contains at least one value!");
        }

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
