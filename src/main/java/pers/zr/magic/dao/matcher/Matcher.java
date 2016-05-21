package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

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

    protected String convertSpecialChar(String value, MatchType matchType) {
        String newValue = value;

        if(MatchType.LIKE == matchType) {
            newValue = newValue.replace("\\", "\\\\");
            newValue = newValue.replace("%", "\\%");
            newValue = newValue.replace("_", "\\_");
        }
        return newValue;
    }

}
