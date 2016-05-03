package pers.zr.magic.dao.core.runtime;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public class ActionValuePair {

    private String column;

    private Object value;

    public ActionValuePair(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
