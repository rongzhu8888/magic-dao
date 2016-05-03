package pers.zr.magic.dao.core.action;

import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.MagicDataSource;
import pers.zr.magic.dao.core.constants.ActionMode;

import java.util.List;

/**
 * 删除处理器：单例模式
 * Created by zhurong on 2016-4-28.
 */
public class DeleteProcessor implements ActionProcessor<Delete> {

    /* 利用私有静态内部类实现延迟加载，首次调用时才会初始化该对象*/
    private static class DeleteProcessorInstanceHolder {
        private static DeleteProcessor instance = new DeleteProcessor();
    }

    private DeleteProcessor() {}

    public static DeleteProcessor getInstance() {
        return DeleteProcessorInstanceHolder.instance;
    }

    @Override
    public ActionResult execute(MagicDataSource dataSource, Delete delete) {

        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(delete.getActionMode());
        String sql = delete.getSql();
        jdbcTemplate.update(sql);
        return null;
    }

    @Override
    public ActionResult[] batchExecute(MagicDataSource dataSource, List<Delete> deleteList) {
        return new ActionResult[0];
    }
}
