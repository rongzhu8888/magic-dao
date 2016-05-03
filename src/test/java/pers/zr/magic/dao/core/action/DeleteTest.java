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
public class DeleteTest extends BaseJunit{

    private static DeleteBuilder deleteBuilder;

    @BeforeClass
    public static void generateDeleteBuilder() {


        deleteBuilder = new DeleteBuilder();
        deleteBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);
    }

    @Test
    public void testGetSql() {

        Delete delete = deleteBuilder.build();
        delete.addCondition(new LessMatcher("finish_time", "2016-05-03 10:00:00"), ConditionType.OR);
        delete.addCondition(new EqualsMatcher("status", 2), ConditionType.AND);
        System.out.println(delete.getSql());

    }

    @Test
    public void testGetParams() {
        Delete delete = deleteBuilder.build();
        delete.addCondition(new LessMatcher("user_id", 1000001), ConditionType.AND);
        delete.addCondition(new EqualsMatcher("status", 2), ConditionType.AND);
        delete.addCondition(new LessMatcher("finish_time", "2016-05-03 10:00:00"), ConditionType.OR);
        Object [] params = delete.getParams();
        System.out.println(params.length);


    }



}
