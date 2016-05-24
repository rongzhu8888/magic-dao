package pers.zr.opensource.magic.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.opensource.magic.dao.constants.ActionMode;

import javax.sql.DataSource;

/**
 *
 * 单一数据源
 *
 * Created by zhurong on 2016-4-28.
 */
public class MagicSingleDataSource implements MagicDataSource {

    private static final Logger log = LogManager.getLogger(MagicSingleDataSource.class);

    private DataSource dataSource;

    private static JdbcTemplate jdbcTemplate;

    private final Object object = new Object();

    @Override
    public JdbcTemplate getJdbcTemplate(ActionMode actionMode) {

        if(null != jdbcTemplate) {
            return jdbcTemplate;
        }

        synchronized (object) {
            if(null == jdbcTemplate) {

                jdbcTemplate = new JdbcTemplate(dataSource);

                if(log.isDebugEnabled()) {
                    log.debug("JdbcTemplate instance created with MagicSingleDataSource!");
                }
            }
        }
        return jdbcTemplate;
    }

    @Override
    public DataSource getJdbcDataSource(ActionMode actionMode) {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
