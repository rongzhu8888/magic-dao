package pers.zr.magic.dao.core.matcher;

import pers.zr.magic.dao.core.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class LessOrEqualsMatcher extends Matcher {

    private Object value;

    public LessOrEqualsMatcher(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public Object[] getValues() {
        return new Object[]{value};
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.LESS_OR_EQUALS;
    }
}
