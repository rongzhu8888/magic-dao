package pers.zr.magic.dao.core.action;

import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.MagicDataSource;
import pers.zr.magic.dao.core.constants.ActionMode;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class QueryProcessor implements ActionProcessor<Query> {

    /* 利用私有静态内部类实现延迟加载，首次调用时才会初始化该对象*/
    private static class QueryProcessorInstanceHolder {
        private static QueryProcessor instance = new QueryProcessor();
    }

    private QueryProcessor() {}

    public static QueryProcessor getInstance() {

        return QueryProcessorInstanceHolder.instance;

    }

    @Override
    public ActionResult execute(MagicDataSource dataSource, Query query) {

        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(ActionMode.QUERY);
        String sql = query.getSql();

        return null;
    }

    @Override
    public ActionResult[] batchExecute(MagicDataSource dataSource, List<Query> queryList) {
        return new ActionResult[0];
    }
}
