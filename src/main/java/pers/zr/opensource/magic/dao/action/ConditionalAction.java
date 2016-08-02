package pers.zr.opensource.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ConditionType;
import pers.zr.opensource.magic.dao.constants.MatchType;
import pers.zr.opensource.magic.dao.matcher.Matcher;
import pers.zr.opensource.magic.dao.shard.TableShardHandler;
import pers.zr.opensource.magic.dao.shard.TableShardStrategy;

import java.util.*;

/**
 * Created by zhurong on 2016/4/30.
 */
public abstract class ConditionalAction extends Action {

    private Collection<Matcher> conditions = new ArrayList<Matcher>();

    private String conSql = null;
    private List<Object> conParams = null;
    private String realTableName = null;

    public <T extends ConditionalAction>T addConditions(Collection<Matcher> matcheres) {
        conditions.addAll(matcheres);
        return (T) this;
    }

    public <T extends ConditionalAction>T addCondition(Matcher matcher) {
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

    protected String getRealTableName() {
        if(null == realTableName) {
            //get actual table name when shard exist
            TableShardStrategy tableShardStrategy = table.getTableShardStrategy();
            TableShardHandler tableShardHandler = table.getTableShardHandler();

            if(null != tableShardHandler && null != tableShardStrategy) {
                String[] shardColumns = tableShardStrategy.getShardColumns();
                List<String> shardColumnList = Arrays.asList(shardColumns);
                List<Object> shardColumnValueList = new ArrayList<Object>();

                for(Matcher matcher : conditions) {
                    if(MatchType.EQUALS == matcher.getMatchType() && shardColumnList.contains(matcher.getColumn())) {
                        shardColumnValueList.add(matcher.getValues()[0]);
                    }
                }
                realTableName = tableShardHandler.getRealTableName(tableShardStrategy,shardColumnValueList.toArray());

                if(tableShardStrategy != null && this.realTableName == null) {
                    throw new RuntimeException("Failed to get real name of shard table!");
                }

            }else {
                realTableName = table.getTableName();
            }
        }
        return realTableName;

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
