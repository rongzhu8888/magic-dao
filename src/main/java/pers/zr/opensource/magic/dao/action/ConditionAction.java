package pers.zr.opensource.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ConditionType;
import pers.zr.opensource.magic.dao.constants.MatchType;
import pers.zr.opensource.magic.dao.matcher.Matcher;
import pers.zr.opensource.magic.dao.utils.ShardUtil;

import java.util.*;

/**
 * Created by zhurong on 2016/4/30.
 */
public abstract class ConditionAction extends Action {

    private Collection<Matcher> conditions = new ArrayList<Matcher>();

    private String conSql = null;
    private List<Object> conParams = null;
    private String shardTableName = null;

    public <T extends ConditionAction>T addConditions(Collection<Matcher> matcheres) {
        conditions.addAll(matcheres);
        return (T) this;
    }

    public <T extends ConditionAction>T addCondition(Matcher matcher) {
        conditions.add(matcher);
        return (T) this;
    }

    protected List<Object> getConParams() {
        if(null == this.conParams) {
            analysisConditions();
        }
        return this.conParams != null ? this.conParams : new ArrayList<Object>(0);
    }

    protected String getConSql() {
        if(null == this.conSql) {
            analysisConditions();
        }
        return this.conSql != null ? this.conSql : "";
    }

    protected String getShardTableName() {
        if(null == shardTableName) {
            analysisConditions();
        }
        return this.shardTableName;
    }


    private void analysisConditions() {

        StringBuilder conditionSqlBuilder = new StringBuilder();
        if(CollectionUtils.isEmpty(conditions)){
            this.conSql = null;

        }else {
            conditionSqlBuilder.append("WHERE ");
            List<Object> conParamsList = new ArrayList<Object>(conditions.size());

            for(Matcher matcher : conditions) {
                conditionSqlBuilder.append(ConditionType.AND).append(" ")
                        .append(matcher.getColumn()).append(" ")
                        .append(matcher.getMatchType().value);

                if(MatchType.IN != matcher.getMatchType()) {
                    conditionSqlBuilder.append(" ? ");

                }else {
                    StringBuilder inConBuilder = new StringBuilder("(");
                    for(int i=0; i<matcher.getValues().length; i++) {
                        inConBuilder.append("?,");
                    }
                    inConBuilder.append(") ");
                    inConBuilder.deleteCharAt(inConBuilder.lastIndexOf(","));
                    conditionSqlBuilder.append(" ").append(inConBuilder);
                }


                getMatcherParam(conParamsList, matcher);

                //具有分表策略时，计算实际表名
                if(null != shardStrategy
                        && matcher.getColumn().equalsIgnoreCase(shardStrategy.getShardColumn())) {
                    this.shardTableName = ShardUtil.getShardTableName(table.getTableName(),
                            shardStrategy.getSeparator(),
                            shardStrategy.getShardCount(),
                            String.valueOf(String.valueOf(matcher.getValues()[0])));
                }
            }

            if(shardStrategy != null && this.shardTableName == null) {
                throw new RuntimeException("Shard error: can not find shard column from condition data!");
            }

            this.conSql = conditionSqlBuilder.toString().replace("WHERE AND", "WHERE");
            this.conParams = conParamsList;

        }

    }

    private void getMatcherParam(List<Object> list, Matcher matcher) {

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
