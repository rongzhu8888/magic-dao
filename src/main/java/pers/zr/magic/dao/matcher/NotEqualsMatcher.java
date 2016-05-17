package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class NotEqualsMatcher extends Matcher {

    private Object value;

    public NotEqualsMatcher(String column, Object value) {

        //转义特殊字符
        if(value instanceof String) {
            value = convertSpecialChar(String.valueOf(value), MatchType.NOT_EQUALS);
        }

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
