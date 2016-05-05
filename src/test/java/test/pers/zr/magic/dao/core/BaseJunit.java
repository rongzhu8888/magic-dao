package test.pers.zr.magic.dao.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.BeforeClass;
import pers.zr.magic.dao.MagicSingleDataSource;
import pers.zr.magic.dao.feature.ShardStrategy;
import pers.zr.magic.dao.runtime.ActionTable;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public class BaseJunit {

    protected static MagicSingleDataSource dataSource = new MagicSingleDataSource();

    protected static ActionTable table ;
    protected static ShardStrategy shardStrategy ;

//    protected static DeleteBuilder deleteBuilder;


    @BeforeClass
    public static void initDataSource() throws Exception{
        String jdbcUrl = "jdbc:mysql://mysql-dev-hdbqrf-01.vip.wgq.hdb.com:3306/db_hdb_laba?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;connectTimeout=5000&amp;socketTimeout=5000";
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

        table = new ActionTable();
        table.setTableName("prize_pool_instances");
        table.setKeys(new String[]{"id"});
        table.setColumns(new String[]{"id", "pool_size","winning_rule_id", "create_time", "update_time","total_lottery_count", "total_received_points","total_paied_points"});


    }
}
