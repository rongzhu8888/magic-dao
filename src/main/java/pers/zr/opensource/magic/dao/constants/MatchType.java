package pers.zr.opensource.magic.dao.constants;

/**
 * Created by zhurong on 2016-4-29.
 */
public enum MatchType {

    EQUALS("="),

    GREATER(">"),

    LESS("<"),

    NOT_EQUALS("!="),

    GREATER_OR_EQUALS(">="),

    LESS_OR_EQUALS("<="),

    IN("IN"),

    LIKE("LIKE"),

    BETWEEN_AND("BETWEEN ? AND");


    public String value;

    MatchType(String value) {
        this.value = value;
    }

}
