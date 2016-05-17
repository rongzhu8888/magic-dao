package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-4-29.
 */
public class InMatcher extends Matcher {

    private Object[] values;

    public InMatcher(String column, Object[] values) {

        //转义特殊字符
        for(int i=0; i<values.length; i++) {
            Object value = values[i];
            if(value instanceof String) {
                value = convertSpecialChar(String.valueOf(value), MatchType.IN);
            }
            values[i] = value;
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
