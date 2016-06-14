package test.pers.zr.magic.dao.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import pers.zr.opensource.magic.dao.MagicSingleDataSource;
import pers.zr.opensource.magic.dao.constants.ActionMode;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhurong on 2016/4/30.
 */
public class MagicSingleQueryDataSourceTest {

    private static MagicSingleDataSource dataSource = new MagicSingleDataSource();

    @BeforeClass
    public static void initDataSource() throws Exception{
        String jdbcUrl = "jdbc:mysql://mysql-dev-hdbqrf-01.vip.wgq.hdb.com:3306/db_hdb_riskcontrol?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;connectTimeout=5000&amp;socketTimeout=5000";
        ComboPooledDataSource c3p0 = new ComboPooledDataSource();
        c3p0.setDriverClass("com.mysql.jdbc.Driver");
        c3p0.setJdbcUrl(jdbcUrl);
        c3p0.setUser("root");
        c3p0.setPassword("p0o9i8u7");
        c3p0.setMaxPoolSize(10);
        c3p0.setMinPoolSize(5);
        c3p0.setInitialPoolSize(5);
        c3p0.setMaxIdleTime(60);
        c3p0.setCheckoutTimeout(3000);
        c3p0.setAcquireIncrement(3);
        c3p0.setAcquireRetryAttempts(0);
        c3p0.setAcquireRetryDelay(1000);
        c3p0.setAutoCommitOnClose(false);
        c3p0.setAutomaticTestTable("conn_test");
        c3p0.setBreakAfterAcquireFailure(false);
        c3p0.setIdleConnectionTestPeriod(60);
        c3p0.setMaxStatements(50);
        c3p0.setMaxStatementsPerConnection(50);
        dataSource.setDataSource(c3p0);

    }

    @Test
    public void testGetJdbcTemplate() {

        JdbcTemplate jdbcTemplate1 = dataSource.getJdbcTemplate(ActionMode.DELETE);
        JdbcTemplate jdbcTemplate2 = dataSource.getJdbcTemplate(ActionMode.QUERY);
        assertEquals(jdbcTemplate1, jdbcTemplate2);

    }


}
