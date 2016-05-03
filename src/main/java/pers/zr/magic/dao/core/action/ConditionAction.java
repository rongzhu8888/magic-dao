package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.constants.ConditionType;
import pers.zr.magic.dao.core.matcher.Matcher;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public abstract class ConditionAction extends Action {

    private Collection<Matcher> andConditions = new ArrayList<Matcher>();

    private Collection<Matcher> orConditions = new ArrayList<Matcher>();

    public <T extends ConditionAction>T addCondition(Matcher matcher, ConditionType type) {

        if(null == matcher) {
            throw new IllegalArgumentException("Invalid parameter: matcher can not be null!");
        }

        if(null == type) {
            throw new IllegalArgumentException("Invalid parameter: type can not be null!");
        }

        if(ConditionType.AND == type) {

            andConditions.add(matcher);

        }else if(ConditionType.OR == type) {

            orConditions.add(matcher);

        }else {

            throw new IllegalArgumentException("Invalid ConditionType, expect [AND] or [OR]");

        }

        return (T) this;
    }

    public Collection<Matcher> getAndConditions() {
        return andConditions;
    }

    public Collection<Matcher> getOrConditions() {
        return orConditions;
    }
}
