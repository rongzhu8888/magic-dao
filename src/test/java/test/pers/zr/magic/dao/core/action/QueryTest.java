package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.opensource.magic.dao.action.ActionBuilderContainer;
import pers.zr.opensource.magic.dao.action.Query;
import pers.zr.opensource.magic.dao.action.QueryBuilder;
import test.pers.zr.magic.dao.core.BaseJunit;
import pers.zr.opensource.magic.dao.matcher.LessMatcher;

/**
 * Created by zhurong on 2016-5-3.
 */
public class QueryTest extends BaseJunit {

    private static QueryBuilder queryBuilder;

    @BeforeClass
    public static void generateBuidler() {
        queryBuilder = new QueryBuilder(table, shardStrategy);
        ActionBuilderContainer.setActionBuilder(queryBuilder);
    }

    @Test
    public void testGetSqlAndParams() {
            Query query = queryBuilder.build();
            query.addCondition(new LessMatcher("user_id", 1000001));

            System.out.println(query.getSql());
            System.out.println(query.getParams().length);
        }

}
