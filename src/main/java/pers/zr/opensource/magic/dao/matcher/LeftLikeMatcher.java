package pers.zr.opensource.magic.dao.matcher;

import pers.zr.opensource.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-5-5.
 */
public class LeftLikeMatcher extends Matcher {

    private String value;

    public LeftLikeMatcher(String column, String value) {

        this.column = column;
        this.value = convertSpecialChar(value, MatchType.LIKE) + "%";
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.LIKE;
    }

    @Override
    public Object[] getValues() {
        return new String[]{value};
    }
}
