package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.action.ActionBuilderContainer;
import pers.zr.magic.dao.action.Query;
import pers.zr.magic.dao.action.QueryBuilder;
import test.pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.constants.ConditionType;
import pers.zr.magic.dao.matcher.EqualsMatcher;
import pers.zr.magic.dao.matcher.LessMatcher;

/**
 * Created by zhurong on 2016-5-3.
 */
public class QueryTest extends BaseJunit {

    private static QueryBuilder queryBuilder;

    @BeforeClass
    public static void generateBuidler() {
        queryBuilder = new QueryBuilder(table);
        ActionBuilderContainer.setActionBuilder(queryBuilder);
    }

    @Test
    public void testGetSqlAndParams() {
            Query query = queryBuilder.build();
            query.addCondition(new LessMatcher("user_id", 1000001), ConditionType.AND);
            query.addCondition(new EqualsMatcher("status", 2), ConditionType.AND);
            query.addCondition(new LessMatcher("finish_time", "2016-05-03 10:00:00"), ConditionType.OR);
            query.setQueryFields("pool_id", "points");

            System.out.println(query.getSql());
            System.out.println(query.getParams().length);
        }

}
