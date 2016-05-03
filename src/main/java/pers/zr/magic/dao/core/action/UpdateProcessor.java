package pers.zr.magic.dao.core.action;

import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.MagicDataSource;
import pers.zr.magic.dao.core.constants.ActionMode;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public class UpdateProcessor implements ActionProcessor<Update> {

    /* 利用私有静态内部类实现延迟加载，首次调用时才会初始化该对象*/
    private static class UpdateProcessorInstanceHolder {
        private static UpdateProcessor processor = new UpdateProcessor();

    }

    private UpdateProcessor() {}

    public static UpdateProcessor getInstance() {

        return UpdateProcessorInstanceHolder.processor;

    }

    @Override
    public ActionResult execute(MagicDataSource dataSource, Update update) {

        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(ActionMode.UPDATE);
        String sql = update.getSql();

        return null;
    }

    @Override
    public ActionResult[] batchExecute(MagicDataSource dataSource, List<Update> updateList) {
        return new ActionResult[0];
    }


}
