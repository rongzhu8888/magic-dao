package pers.zr.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.constants.ConditionType;
import pers.zr.magic.dao.matcher.Matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public abstract class ConditionAction extends Action {

    private Collection<Matcher> andConditions = new ArrayList<Matcher>();

    private Collection<Matcher> orConditions = new ArrayList<Matcher>();

    private String conSql = null;
    private List<Object> conParams = new ArrayList<Object>(0);

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

    public List<Object> getConParams() {
        if(this.conParams.size() == 0) {
            analysisCondtions();
        }
        return this.conParams;
    }

    public String getConSql() {
        if(null == this.conSql) {
            analysisCondtions();
        }
        return this.conSql != null ? this.conSql : "";
    }

    private void analysisCondtions() {

        StringBuilder conditionSqlBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(andConditions) && CollectionUtils.isEmpty(orConditions)) {
            this.conSql = null;

        }else {
            conditionSqlBuilder.append("WHERE ");
            List<Object> conParamsList = new ArrayList<Object>(andConditions.size() + orConditions.size());

            if(!CollectionUtils.isEmpty(andConditions)) {

                for(Matcher matcher : andConditions) {
                    conditionSqlBuilder.append(ConditionType.AND).append(" ")
                            .append(matcher.getColumn()).append(" ")
                            .append(matcher.getMatchType().value).append(" ? ");

                    convertMatcher(conParamsList, matcher);
                }
            }
            if(!CollectionUtils.isEmpty(orConditions)) {

                for(Matcher matcher : orConditions) {
                    conditionSqlBuilder.append(ConditionType.OR).append(" ")
                            .append(matcher.getColumn()).append(" ")
                            .append(matcher.getMatchType().value).append(" ? ");

                    convertMatcher(conParamsList, matcher);
                }

            }

            this.conSql = conditionSqlBuilder.toString().replace("WHERE AND", "WHERE");
            this.conSql = this.conSql.replace("WHERE OR", "WHERE");
            this.conParams = conParamsList;

        }

    }

    private void convertMatcher(List<Object> list, Matcher matcher) {

        if(null == matcher) {
            return;
        }

        if(null == list) {
            list = new ArrayList<Object>();
        }

        switch (matcher.getMatchType()) {
            case EQUALS :
            case GREATER :
            case GREATER_OR_EQUALS :
            case LESS :
            case LESS_OR_EQUALS :
            case NOT_EQUALS :
            case LIKE :
                list.add(matcher.getValues()[0]);
                break;

            case BETWEEN_AND :
                list.add(matcher.getValues()[0]);
                list.add(matcher.getValues()[1]);
                break;

            case IN :
                list.addAll(Arrays.asList(matcher.getValues()));
                break;

            default:
                throw new IllegalArgumentException("Invalid match type : " + matcher.getMatchType());

        }

    }
}
