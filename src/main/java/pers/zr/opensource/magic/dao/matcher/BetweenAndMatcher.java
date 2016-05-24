package pers.zr.opensource.magic.dao.matcher;

import pers.zr.opensource.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class BetweenAndMatcher extends Matcher {

    private Object minValue;

    private Object maxValue;

    public BetweenAndMatcher(String column, Object minValue, Object maxValue) {
        this.column = column;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.BETWEEN_AND;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{this.minValue, this.maxValue};
    }
}
