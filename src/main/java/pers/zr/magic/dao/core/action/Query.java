package pers.zr.magic.dao.core.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.constants.ConditionType;
import pers.zr.magic.dao.core.matcher.Matcher;

import java.util.Collection;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Query extends ConditionAction {

    private Logger log = LogManager.getLogger(Delete.class);

    private String[] queryFields;

    private String sql;
    private Object[] params;

    public void setQueryFields(String... queryFields) {
        this.queryFields = queryFields;
    }

    @Override
    public String getSql() {

        if(null == this.sql) {
            analysis();
        }
        return this.sql;
    }

    @Override
    public Object[] getParams() {

        if(null == this.params) {
            analysis();
        }
        return this.params != null ? this.params : new Object[0];
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.QUERY;
    }

    private void analysis() {
        String targetFields;
        if(null!=queryFields && queryFields.length>0) {
            StringBuilder fieldsBuilder = new StringBuilder();
            for(String field : queryFields) {
                fieldsBuilder.append(field).append(",");
            }
            targetFields = fieldsBuilder.deleteCharAt(fieldsBuilder.lastIndexOf(",")).toString();
        }else {
            targetFields = "*";
        }

        String sql;
        StringBuilder sqlBuilder = new StringBuilder("SELECT ").append(targetFields).append(" ");
        sqlBuilder.append("FROM ").append(getTable().getTableName()).append(" ");
        Collection<Matcher> andConditions = getAndConditions();
        Collection<Matcher> orConditions = getOrConditions();
        if(CollectionUtils.isEmpty(andConditions) && CollectionUtils.isEmpty(orConditions)) {
            sql = sqlBuilder.toString();

        }else {
            sqlBuilder.append("WHERE ");

            int paramsCount = andConditions.size() + orConditions.size();
            Object [] params = new Object[paramsCount];
            int paramIndex = 0;

            if(!CollectionUtils.isEmpty(andConditions)) {

                for(Matcher condition : andConditions) {
                    sqlBuilder.append(ConditionType.AND).append(" ")
                            .append(condition.getColumn()).append(" ")
                            .append(condition.getMatchType().value).append(" ").append("? ");

                    params[paramIndex] = condition.getValues()[0];
                    paramIndex++;
                }
            }
            if(!CollectionUtils.isEmpty(orConditions)) {

                for(Matcher condition : orConditions) {
                    sqlBuilder.append(ConditionType.OR).append(" ")
                            .append(condition.getColumn()).append(" ")
                            .append(condition.getMatchType().value).append(" ").append("?");

                    params[paramIndex] = condition.getValues()[0];
                    paramIndex++;
                }

            }

            sql = sqlBuilder.toString().replace("WHERE AND", "WHERE");
            sql = sql.replace("WHERE OR", "WHERE");
            this.params = params;

        }

        this.sql = sql;

        if(log.isDebugEnabled()) {
            log.debug("Query SQL: ###" + sql + "###");
        }

    }
}
