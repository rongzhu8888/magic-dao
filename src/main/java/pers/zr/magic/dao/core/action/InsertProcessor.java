package pers.zr.magic.dao.core.action;

import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.MagicDataSource;
import pers.zr.magic.dao.core.constants.ActionMode;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class InsertProcessor implements ActionProcessor<Insert> {

    /* 利用私有静态内部类实现延迟加载，首次调用时才会初始化该对象*/
    private static class InsertProcessorInstanceHolder {

        private static InsertProcessor instance = new InsertProcessor();

    }

    private InsertProcessor() {}

    public static InsertProcessor getInstance() {

        return InsertProcessorInstanceHolder.instance;

    }


    @Override
    public ActionResult execute(MagicDataSource dataSource, Insert insert) {


        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(ActionMode.INSERT);

        String sql = insert.getSql();

        return null;
    }

    @Override
    public ActionResult[] batchExecute(MagicDataSource dataSource, List<Insert> insertList) {
        return new ActionResult[0];
    }
}
