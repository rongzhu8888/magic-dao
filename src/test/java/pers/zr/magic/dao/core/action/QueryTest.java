package pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.core.constants.ConditionType;
import pers.zr.magic.dao.core.matcher.EqualsMatcher;
import pers.zr.magic.dao.core.matcher.LessMatcher;

/**
 * Created by zhurong on 2016-5-3.
 */
public class QueryTest extends BaseJunit {

    private static QueryBuilder queryBuilder;

    @BeforeClass
    public static void generateBuidler() {
        queryBuilder = new QueryBuilder();
        queryBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(queryBuilder);
    }

    @Test
    public void testGetParams() {
        for(int i=0; i<10; i++) {
            long time1 = System.currentTimeMillis();
            Query query = queryBuilder.build();
            query.addCondition(new LessMatcher("user_id", 1000001), ConditionType.AND);
            query.addCondition(new EqualsMatcher("status", 2), ConditionType.AND);
            query.addCondition(new LessMatcher("finish_time", "2016-05-03 10:00:00"), ConditionType.OR);
            query.setQueryFields("pool_id", "points");

            Object[] params = query.getParams();
            String sql = query.getSql();
            long time2 = System.currentTimeMillis();
            System.out.println("costs " + (time2 - time1));
//            System.out.println(sql);
//            System.out.println(params.length);
        }
    }
}
