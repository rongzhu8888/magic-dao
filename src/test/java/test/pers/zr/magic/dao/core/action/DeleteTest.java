package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.action.ActionBuilderContainer;
import pers.zr.magic.dao.action.Delete;
import pers.zr.magic.dao.action.DeleteBuilder;
import test.pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.constants.ConditionType;
import pers.zr.magic.dao.matcher.EqualsMatcher;
import pers.zr.magic.dao.matcher.LessMatcher;

/**
 * Created by zhurong on 2016-5-3.
 */
public class DeleteTest extends BaseJunit{

    private static DeleteBuilder deleteBuilder;

    @BeforeClass
    public static void generateDeleteBuilder() {
        deleteBuilder = new DeleteBuilder(table);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);
    }


    @Test
    public void testGetSqlAndParams() {
        Delete delete = deleteBuilder.build();
        delete.addCondition(new LessMatcher("user_id", 1000001), ConditionType.AND);
        delete.addCondition(new EqualsMatcher("status", 2), ConditionType.AND);
        delete.addCondition(new LessMatcher("finish_time", "2016-05-03 10:00:00"), ConditionType.OR);
        System.out.println(delete.getSql());
        System.out.println(delete.getParams().length);


    }



}
