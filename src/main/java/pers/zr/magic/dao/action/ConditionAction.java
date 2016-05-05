package pers.zr.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.constants.ConditionType;
import pers.zr.magic.dao.constants.MatchType;
import pers.zr.magic.dao.matcher.Matcher;
import pers.zr.magic.dao.utils.ShardingUtil;

import java.util.*;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public abstract class ConditionAction extends Action {

    private Collection<Matcher> conditions = new ArrayList<Matcher>();

    private String conSql = null;
    private List<Object> conParams = null;
    private String shardingTableName = null;

    public <T extends ConditionAction>T addCondition(Matcher matcher) {
        conditions.add(matcher);
        return (T) this;
    }

    public List<Object> getConParams() {
        if(null == this.conParams) {
            analysisConditions();
        }
        return this.conParams != null ? this.conParams : new ArrayList<Object>(0);
    }

    public String getConSql() {
        if(null == this.conSql) {
            analysisConditions();
        }
        return this.conSql != null ? this.conSql : "";
    }

    public String getShardingTableName() {
        if(null == shardingTableName) {
            analysisConditions();
        }
        return this.shardingTableName;
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
                        .append(matcher.getMatchType().value).append(" ? ");

                convertMatcher(conParamsList, matcher);

                //具有分表策略时，计算实际表名
                if(null != shardStrategy
                        && matcher.getColumn().equalsIgnoreCase(shardStrategy.getShardingColumn())) {

                    this.shardingTableName = this.table.getTableName()
                            + this.shardStrategy.getConnector()
                            + ShardingUtil.getShardingTableSuffix(String.valueOf(matcher.getValues()[0]), shardStrategy.getShardingCount());
                }
            }

            if(shardStrategy != null && this.shardingTableName == null) {
                throw new RuntimeException("Shard error: can not find shard column from conditions!");
            }

            this.conSql = conditionSqlBuilder.toString().replace("WHERE AND", "WHERE");
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
