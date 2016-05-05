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
        deleteBuilder = new DeleteBuilder(table, shardStrategy);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);
    }


    @Test
    public void testGetSqlAndParams() {
        Delete delete = deleteBuilder.build();
        delete.addCondition(new EqualsMatcher("user_id", 100000010099L));
        System.out.println(delete.getSql());
        System.out.println(delete.getParams().length);


    }



}
