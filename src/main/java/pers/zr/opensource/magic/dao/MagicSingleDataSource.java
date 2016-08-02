package pers.zr.opensource.magic.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.opensource.magic.dao.constants.ActionMode;

import javax.sql.DataSource;

/**
 *
 * Single DataSource
 *
 * Created by zhurong on 2016-4-28.
 */
public class MagicSingleDataSource implements MagicDataSource {

    private Log log = LogFactory.getLog(MagicSingleDataSource.class);

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;


    @Override
    public JdbcTemplate getJdbcTemplate(ActionMode actionMode) {
        return jdbcTemplate;
    }

    @Override
    public DataSource getJdbcDataSource(ActionMode actionMode) {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        if(log.isDebugEnabled()) {
            log.debug("JdbcTemplate instance created with MagicSingleDataSource!");
        }
    }
}
