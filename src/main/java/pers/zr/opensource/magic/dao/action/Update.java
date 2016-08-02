package pers.zr.opensource.magic.dao.action;

import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ActionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Update extends ConditionalAction {

    private Map<String, Object> updateFields;

    public void setUpdateFields(Map<String, Object> updateFields) {
        this.updateFields = updateFields;
    }

    @Override
    public String getSql() {

        if(null == this.sql) {
            parse();
        }

        if (log.isDebugEnabled()) {
            log.debug("### [ " + sql + "] ###");
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
        return ActionMode.UPDATE;
    }

    private void parse() {

        if(CollectionUtils.isEmpty(updateFields)) {
            throw new RuntimeException("update fields can not be empty!");
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(getRealTableName()).append(" SET ");
        List<Object> paramsList = new ArrayList<Object>();
        for(String column : updateFields.keySet()) {
            sqlBuilder.append(column).append("=?,");
            paramsList.add(updateFields.get(column));
        }

        sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(",")).append(" ");
        sqlBuilder.append(getConSql());
        paramsList.addAll(getConParams());

        this.sql = sqlBuilder.toString();
        this.params = paramsList.toArray();

    }
}
