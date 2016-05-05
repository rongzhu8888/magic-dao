package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class NotEqualsMatcher extends Matcher {

    private Object value;

    public NotEqualsMatcher(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public Object[] getValues() {
        return new Object[]{value};
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.NOT_EQUALS;
    }
}
