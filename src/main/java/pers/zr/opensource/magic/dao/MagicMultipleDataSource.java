package pers.zr.opensource.magic.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import pers.zr.opensource.magic.dao.runtime.RuntimeQueryDataSource;
import pers.zr.opensource.magic.dao.constants.ActionMode;
import pers.zr.opensource.magic.dao.constants.DataSourceType;

import javax.sql.DataSource;
import java.util.*;

/**
 * Multiple DataSource
 *
 * For Reading and writing separation
 *
 * Created by zhurong on 2016-4-28.
 */
public class MagicMultipleDataSource implements MagicDataSource {

    private Log log = LogFactory.getLog(MagicMultipleDataSource.class);

    private DataSource master;
    private Map<String, DataSource> slaves;

    private JdbcTemplate masterJdbcTemplate;
    private Map<String, JdbcTemplate> slaveJdbcTemplates;

    private List<String> dataSourceNameList;

    @Override
    public JdbcTemplate getJdbcTemplate(ActionMode actionMode) {
        if(CollectionUtils.isEmpty(this.slaves)) {
            throw new RuntimeException("MagicMultipleDataSource must has at least one slave DataSource!");
        }

        boolean isMasterDataSource;
        String currentQueryDataSourceName = RuntimeQueryDataSource.alias.get();
        DataSourceType currentDataSourceType = RuntimeQueryDataSource.type.get();

        if(ActionMode.INSERT == actionMode || ActionMode.UPDATE == actionMode || ActionMode.DELETE == actionMode) {
            //insert|update|delete on master
            isMasterDataSource = true;

        } else if(ActionMode.QUERY == actionMode){
            if(null == currentQueryDataSourceName && DataSourceType.MASTER == currentDataSourceType) {
                //force to read from master
                isMasterDataSource = true;
            }else {
                isMasterDataSource = false;
            }

        }else {
            throw new RuntimeException("Invalid action mode!");
        }

        if(isMasterDataSource) {
            return masterJdbcTemplate;
        }else {
            if(null != currentQueryDataSourceName) {
                return slaveJdbcTemplates.get(currentQueryDataSourceName);
            }else {
                //random slave
                int randomSlaveIndex = new Random().nextInt(slaves.size());
                return slaveJdbcTemplates.get(dataSourceNameList.get(randomSlaveIndex));
            }
        }

    }

    @Override
    public DataSource getJdbcDataSource(ActionMode actionMode) {
        if(CollectionUtils.isEmpty(this.slaves)) {
            throw new RuntimeException("MagicMultipleDataSource must has at least one slave QueryDataSource!");
        }
        DataSource dataSource;
        String currentQueryDataSourceName = RuntimeQueryDataSource.alias.get();
        DataSourceType currentDataSourceType = RuntimeQueryDataSource.type.get();

        if(ActionMode.INSERT == actionMode || ActionMode.UPDATE == actionMode || ActionMode.DELETE == actionMode) {
            dataSource = master;

        }else if(ActionMode.QUERY == actionMode) {
            if(null != currentQueryDataSourceName) {
                dataSource = slaves.get(currentQueryDataSourceName);

            }else if(DataSourceType.MASTER == currentDataSourceType) {
                dataSource = master;

            }else {
                int randomSlaveIndex = new Random().nextInt(slaves.size());
                dataSource = slaves.get(dataSourceNameList.get(randomSlaveIndex));

            }

        }else {
            throw new RuntimeException("Invalid action mode!");

        }
        return dataSource;
    }

    public void setMaster(DataSource master) {
        this.master = master;
        this.masterJdbcTemplate = new JdbcTemplate(this.master);
        if(log.isDebugEnabled()) {
            log.debug("JdbcTemplate instance created with master of MagicMultipleDataSource!");
        }

    }

    public void setSlaves(Map<String, DataSource> slaves) {
        if(CollectionUtils.isEmpty(slaves)) {
            throw new RuntimeException("MagicMultipleDataSource must has at least one slave DataSource!");
        }
        this.slaves = slaves;
        slaveJdbcTemplates = new HashMap<String, JdbcTemplate>(slaves.size());
        dataSourceNameList = new ArrayList<String>(slaves.size());
        Set<Map.Entry<String, DataSource>> entrySet = slaves.entrySet();
        for(Map.Entry<String, DataSource> entry : entrySet) {
            String dsAlias = entry.getKey();
            DataSource ds = entry.getValue();
            dataSourceNameList.add(dsAlias);
            slaveJdbcTemplates.put(dsAlias, new JdbcTemplate(ds));
            if(log.isDebugEnabled()) {
                log.debug("JdbcTemplate instance created with slaves of MagicMultipleDataSource!");
            }
        }

    }
}
