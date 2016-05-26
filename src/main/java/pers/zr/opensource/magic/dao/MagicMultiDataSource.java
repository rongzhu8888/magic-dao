package pers.zr.opensource.magic.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.constants.DataSourceType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Multiple DataSource
 *
 * For Reading and writing separation
 *
 * Created by zhurong on 2016-4-28.
 */
public class MagicMultiDataSource implements MagicDataSource {

    private Logger log = LogManager.getLogger(MagicMultiDataSource.class);

    public static ThreadLocal<DataSourceType> runtimeReadingDataSourceType = new ThreadLocal<DataSourceType>();

    private DataSource master;

    private List<DataSource> slaves;

    private static JdbcTemplate masterJdbcTemplate;
    private static List<JdbcTemplate> slaveJdbcTemplates;

    private final Object object1 = new Object();
    private final Object object2 = new Object();

    @Override
    public JdbcTemplate getJdbcTemplate(ActionMode actionMode) {
        if(CollectionUtils.isEmpty(this.slaves)) {
            throw new RuntimeException("MagicMultiDataSource must has at least one slave DataSource!");
        }

        boolean isNowMasterDataSource;
        if(ActionMode.INSERT == actionMode || ActionMode.UPDATE == actionMode || ActionMode.DELETE == actionMode) {
            //insert|update|delete on master
            isNowMasterDataSource = true;

        } else if(ActionMode.QUERY == actionMode){
            if(DataSourceType.MASTER == runtimeReadingDataSourceType.get()) {
                //force to read from slave
                isNowMasterDataSource = true;
            }else {
                isNowMasterDataSource = false;
            }

        }else {
            throw new RuntimeException("Invalid action mode!");
        }

        if(isNowMasterDataSource) {
            if(null != masterJdbcTemplate) {
                return masterJdbcTemplate;
            }
            synchronized (object1) {
                if(null == masterJdbcTemplate) {
                    if(log.isDebugEnabled()) {
                        log.debug("JdbcTemplate instance created with master of MagicMultiDataSource!");
                    }
                    masterJdbcTemplate = new JdbcTemplate(master);
                }
            }
            return masterJdbcTemplate;
        }else {
            if(null == slaveJdbcTemplates || slaveJdbcTemplates.isEmpty()) {
                synchronized (object2) {
                    if(null == slaveJdbcTemplates || slaveJdbcTemplates.isEmpty()) {
                        slaveJdbcTemplates = new ArrayList<JdbcTemplate>();
                        for(DataSource slave : slaves) {
                            if(log.isDebugEnabled()) {
                                log.debug("JdbcTemplate instance created with slaves of MagicMultiDataSource!");
                            }
                            slaveJdbcTemplates.add(new JdbcTemplate(slave));
                        }
                    }
                }
            }

            //random slave
            int randomSlaveIndex = new Random().nextInt(slaves.size());
            return slaveJdbcTemplates.get(randomSlaveIndex);
        }

    }

    @Override
    public DataSource getJdbcDataSource(ActionMode actionMode) {
        if(CollectionUtils.isEmpty(this.slaves)) {
            throw new RuntimeException("MagicMultiDataSource must has at least one slave DataSource!");
        }
        DataSource dataSource;
        if(ActionMode.INSERT == actionMode || ActionMode.UPDATE == actionMode || ActionMode.DELETE == actionMode) {
            dataSource = master;
        }else if(ActionMode.QUERY == actionMode) {
            if(DataSourceType.MASTER == runtimeReadingDataSourceType.get()) {
                dataSource = master;
            }else {
                int randomSlaveIndex = new Random().nextInt(slaves.size());
                dataSource = slaves.get(randomSlaveIndex);
            }
        }else {
            throw new RuntimeException("Invalid action mode!");
        }
        return dataSource;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
        if(CollectionUtils.isEmpty(this.slaves)) {
            throw new RuntimeException("MagicMultiDataSource must has at least one slave DataSource!");
        }
    }
}
