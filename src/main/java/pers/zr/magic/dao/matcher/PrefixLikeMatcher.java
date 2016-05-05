package pers.zr.magic.dao.matcher;

import pers.zr.magic.dao.constants.MatchType;

/**
 * Created by zhurong on 2016-5-5.
 */
public class PrefixLikeMatcher extends Matcher {

    private String value;

    public PrefixLikeMatcher(String column, String value) {

        this.column = column;
        this.value = value.replace("_", "\\_").replace("%", "\\%") + "%"; //转义_、%特殊字符
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
