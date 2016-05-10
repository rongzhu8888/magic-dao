package pers.zr.magic.dao.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.order.Order;
import pers.zr.magic.dao.page.PageModel;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class Query extends ConditionAction {

    private Logger log = LogManager.getLogger(Query.class);

    private List<String> queryFields;

    private PageModel pageModel;

    private Order[] orders;

    public void setQueryFields(List<String> queryFields) {
        this.queryFields = queryFields;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    public void setOrders(Order[] orders) {
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
                tableName = getShardingTableName();
            }

            sqlBuilder.append("SELECT ").append(targetFieldsStr).append(" FROM ")
                    .append(tableName).append(" ").append(getConSql());

            //排序
            if(null != orders && orders.length > 0) {
                sqlBuilder.append(" ORDER BY ");
                for(int i=0; i<orders.length; i++) {
                    sqlBuilder.append(orders[i].getColumn()).append(" ").append(orders[i].getType()).append(", ");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
            }

            //分页
            if(null != pageModel) {
                int limit = pageModel.getPageSize();
                int offset = (pageModel.getPageNumber() - 1) * limit;
                sqlBuilder.append("LIMIT ? OFFSET ? ");
            }

            this.sql = sqlBuilder.toString();

            if (log.isDebugEnabled()) {
                log.debug("### Query SQL: " + sql + "###");
            }
        }
        return this.sql;


    }

    @Override
    public Object[] getParams() {

        List<Object> paramsList = getConParams();
        //分页
        if(null != pageModel) {
            int limit = pageModel.getPageSize();
            int offset = (pageModel.getPageNumber() - 1) * limit;
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
