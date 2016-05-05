package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.zr.magic.dao.constants.ActionMode;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Query extends ConditionAction {

    private Logger log = LogManager.getLogger(Query.class);

    private String[] queryFields;

    public void setQueryFields(String... queryFields) {
        this.queryFields = queryFields;
    }

    @Override
    public String getSql() {
        if(null == sql) {
            String targetFieldsStr;
            if(null!=queryFields && queryFields.length>0) {
                StringBuilder fieldsBuilder = new StringBuilder();
                for(String field : queryFields) {
                    fieldsBuilder.append(field).append(",");
                }
                targetFieldsStr = fieldsBuilder.deleteCharAt(fieldsBuilder.lastIndexOf(",")).toString();
            }else {
                targetFieldsStr = "*";
            }

            this.sql = (new StringBuilder()).append("SELECT ").append(targetFieldsStr).append(" FROM ")
                    .append(table.getTableName()).append(" ").append(getConSql()).toString();

            if (log.isDebugEnabled()) {
                log.debug("Query SQL: ###" + sql + "###");
            }
        }
        return this.sql;


    }

    @Override
    public Object[] getParams() {
        this.params = getConParams().toArray();
        return this.params;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.QUERY;
    }


}
