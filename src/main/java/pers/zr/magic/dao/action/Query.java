package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.order.Order;
import pers.zr.magic.dao.page.Page;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Query extends ConditionAction {

    private Logger log = LogManager.getLogger(Query.class);

    private List<String> queryFields;

    private Page page;

    private List<Order> orders;

    public void setQueryFields(List<String> queryFields) {
        this.queryFields = queryFields;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String getSql() {
        if(null == sql) {

            StringBuilder sqlBuilder = new StringBuilder();
            String targetFieldsStr;
            if(null!=queryFields && queryFields.size()>0) {
                StringBuilder fieldsBuilder = new StringBuilder();
                for(String field : queryFields) {
                    fieldsBuilder.append(field).append(",");
                }
                targetFieldsStr = fieldsBuilder.deleteCharAt(fieldsBuilder.lastIndexOf(",")).toString();
            }else {
                targetFieldsStr = "*";
            }

            String tableName = this.table.getTableName();
            if(null != this.shardStrategy) {
                tableName = getShardTableName();
            }

            sqlBuilder.append("SELECT ").append(targetFieldsStr).append(" FROM ")
                    .append(tableName).append(" ").append(getConSql());

            //排序
            if(null != orders && orders.size() > 0) {
                sqlBuilder.append(" ORDER BY ");
                for(Order order : orders) {
                    sqlBuilder.append(order.getColumn()).append(" ").append(order.getType()).append(", ");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
            }

            //分页
            if(null != page) {
                int limit = page.getPageSize();
                int offset = (page.getPageNo() - 1) * limit;
                sqlBuilder.append("LIMIT ? OFFSET ? ");
            }

            this.sql = sqlBuilder.toString();

        }

        if (log.isDebugEnabled()) {
            log.debug("### [ " + sql + "] ###");
        }

        return this.sql;


    }

    @Override
    public Object[] getParams() {

        List<Object> paramsList = getConParams();
        //分页
        if(null != page) {
            int limit = page.getPageSize();
            int offset = (page.getPageNo() - 1) * limit;
            paramsList.add(limit);
            paramsList.add(offset);
        }

        this.params = paramsList.toArray();
        return this.params;

    }

    @Override
    public ActionMode getActionMode() {
        return ActionMode.QUERY;
    }


}
