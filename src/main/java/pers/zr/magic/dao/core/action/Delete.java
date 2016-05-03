package pers.zr.magic.dao.core.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.constants.ConditionType;
import pers.zr.magic.dao.core.matcher.Matcher;
import pers.zr.magic.dao.core.runtime.ActionCondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Delete extends ConditionAction {

    private Logger log = LogManager.getLogger(Delete.class);

    private String sql;
    private Object[] params;

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
        return ActionMode.DELETE;
    }

    private void analysis() {
        String sql;
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ");
        sqlBuilder.append(getTable().getTableName()).append(" ");
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
            log.debug("Delete SQL: ###" + sql + "###");
        }

    }



}
