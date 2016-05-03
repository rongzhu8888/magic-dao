package pers.zr.magic.dao.core;

import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.constants.ActionMode;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 多数据源，用于读写分离
 *
 * Created by zhurong on 2016-4-28.
 */
public class MagicMultiDataSource implements MagicDataSource {

    private DataSource master;

    private List<DataSource> slaves;

    private static JdbcTemplate masterJdbcTemplate;
    private static List<JdbcTemplate> slaveJdbcTemplates;

    private final Object object1 = new Object();
    private final Object object2 = new Object();

    @Override
    public JdbcTemplate getJdbcTemplate(ActionMode actionMode) {
        if(ActionMode.QUERY != actionMode)
        {  //insert|update|delete操作使用主库

            if(null != masterJdbcTemplate) {
                return masterJdbcTemplate;
            }

            synchronized (object1) {
                if(null == masterJdbcTemplate) {

                    masterJdbcTemplate = new JdbcTemplate(master);
                }
            }
            return masterJdbcTemplate;

        }
        else
        {  //query操作，如果没有从库，则查询主库，否则优先查询从库
            if(slaves == null || slaves.isEmpty()) {
                return masterJdbcTemplate;
            }else {

                if(null == slaveJdbcTemplates || slaveJdbcTemplates.isEmpty()) {
                    synchronized (object2) {
                        if(null == slaveJdbcTemplates || slaveJdbcTemplates.isEmpty()) {

                            slaveJdbcTemplates = new ArrayList<JdbcTemplate>();
                            for(DataSource slave : slaves) {
                                slaveJdbcTemplates.add(new JdbcTemplate(slave));
                            }
                        }
                    }
                }

                //TODO 自动移除连接断开或者不可用的从库

                //随机返回一个从库
                //TODO 待优化，可以考虑类似负载均衡
                int randomSlaveIndex = new Random().nextInt(slaves.size());
                return slaveJdbcTemplates.get(randomSlaveIndex);
            }
        }

    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public void setSlaves(List<DataSource> slaves) {
        this.slaves = slaves;
    }
}
