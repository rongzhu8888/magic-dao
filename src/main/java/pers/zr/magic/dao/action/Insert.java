package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.constants.ActionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Insert extends Action {

    private static Logger log = LogManager.getLogger(Insert.class);

    private Map<String, Object> insertFields;

    public void setInsertFields(Map<String, Object> insertFields) {
        this.insertFields = insertFields;
    }

    @Override
    public String getSql() {

        if(null == this.sql) {
            parse();
            if (log.isDebugEnabled()) {
                log.debug("Insert SQL: ###" + sql + "###");
            }
        }
        return this.sql;
    }

    @Override
    public Object[] getParams() {

        if(null == this.params) {
            parse();
        }
        return this.params;
    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.INSERT;
    }


    private void parse() {

        if(CollectionUtils.isEmpty(insertFields)) {
            throw new RuntimeException("insert fields can not be empty!");
        }

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(table.getTableName()).append(" (");
        List<Object> paramsList = new ArrayList<Object>(insertFields.size());

        for(String column : insertFields.keySet()) {
            sqlBuilder.append(column).append(",");
            paramsList.add(insertFields.get(column));
        }

        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(") VALUES (");
        for(int i=0; i<insertFields.size(); i++) {
            sqlBuilder.append("?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(")");

        this.sql = sqlBuilder.toString();
        this.params = paramsList.toArray();


    }
}
