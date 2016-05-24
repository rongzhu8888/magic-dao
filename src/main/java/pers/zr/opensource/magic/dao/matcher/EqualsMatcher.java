package pers.zr.opensource.magic.dao.matcher;

import pers.zr.opensource.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class EqualsMatcher extends Matcher {

    private Object value;

    public EqualsMatcher(String column, Object value) {

        this.value = value;
        this.column = column;
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.EQUALS;
    }

    public Object[] getValues() {
        return new Object[]{value};
    }
}
