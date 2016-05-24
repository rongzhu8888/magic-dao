package pers.zr.opensource.magic.dao.matcher;

import pers.zr.opensource.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-5-5.
 */
public class RightLikeMatcher extends Matcher {

    private String value;

    public RightLikeMatcher(String column, String value) {

        this.column = column;
        this.value = "%" + convertSpecialChar(value, MatchType.LIKE); //转义特殊字符
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
