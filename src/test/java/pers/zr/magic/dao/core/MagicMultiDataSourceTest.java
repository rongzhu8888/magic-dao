package pers.zr.magic.dao.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.magic.dao.core.constants.ActionMode;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public class MagicMultiDataSourceTest {

    private static MagicMultiDataSource dataSource = new MagicMultiDataSource();

    @BeforeClass
    public static void initDataSource() throws Exception {

        dataSource.setMaster(getMaster());

        List<DataSource> slaveList = new ArrayList<>();
        slaveList.add(getSlave());
        dataSource.setSlaves(slaveList);

    }

    @Test
    public void testGetJdbcTemplate() {

        JdbcTemplate jdbcTemplate1 = dataSource.getJdbcTemplate(ActionMode.QUERY);
        JdbcTemplate jdbcTemplate2 = dataSource.getJdbcTemplate(ActionMode.DELETE);
        JdbcTemplate jdbcTemplate3 = dataSource.getJdbcTemplate(ActionMode.UPDATE);
        JdbcTemplate jdbcTemplate4 = dataSource.getJdbcTemplate(ActionMode.INSERT);

        assertNotEquals(jdbcTemplate1, jdbcTemplate2);
        assertEquals(jdbcTemplate2, jdbcTemplate3);
        assertEquals(jdbcTemplate2, jdbcTemplate4);



    }


    private static ComboPooledDataSource getMaster() throws Exception {
        String jdbcUrl = "jdbc:mysql://mysql-dev-hdbqrf-01.vip.wgq.hdb.com:3306/db_hdb_riskcontrol?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;connectTimeout=5000&amp;socketTimeout=5000";
        ComboPooledDataSource master = new ComboPooledDataSource();
        master.setDriverClass("com.mysql.jdbc.Driver");
        master.setJdbcUrl(jdbcUrl);
        master.setUser("root");
        master.setPassword("p0o9i8u7");
        master.setMaxPoolSize(10);
        master.setMinPoolSize(5);
        master.setInitialPoolSize(5);
        master.setMaxIdleTime(60);
        master.setCheckoutTimeout(3000);
        master.setAcquireIncrement(3);
        master.setAcquireRetryAttempts(0);
        master.setAcquireRetryDelay(1000);
        master.setAutoCommitOnClose(false);
        master.setAutomaticTestTable("conn_test");
        master.setBreakAfterAcquireFailure(false);
        master.setIdleConnectionTestPeriod(60);
        master.setMaxStatements(50);
        master.setMaxStatementsPerConnection(50);
        return master;
    }


    private static ComboPooledDataSource getSlave() throws Exception {
        String jdbcUrl = "jdbc:mysql://mysql-dev-hdbqrf-01.vip.wgq.hdb.com:3306/db_hdb_riskcontrol?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;connectTimeout=5000&amp;socketTimeout=5000";
        ComboPooledDataSource slave = new ComboPooledDataSource();
        slave.setDriverClass("com.mysql.jdbc.Driver");
        slave.setJdbcUrl(jdbcUrl);
        slave.setUser("root");
        slave.setPassword("p0o9i8u7");
        slave.setMaxPoolSize(10);
        slave.setMinPoolSize(5);
        slave.setInitialPoolSize(5);
        slave.setMaxIdleTime(60);
        slave.setCheckoutTimeout(3000);
        slave.setAcquireIncrement(3);
        slave.setAcquireRetryAttempts(0);
        slave.setAcquireRetryDelay(1000);
        slave.setAutoCommitOnClose(false);
        slave.setAutomaticTestTable("conn_test");
        slave.setBreakAfterAcquireFailure(false);
        slave.setIdleConnectionTestPeriod(60);
        slave.setMaxStatements(50);
        slave.setMaxStatementsPerConnection(50);
        return slave;
    }

}
