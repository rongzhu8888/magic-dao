package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.opensource.magic.dao.action.ActionBuilderContainer;
import pers.zr.opensource.magic.dao.action.Delete;
import pers.zr.opensource.magic.dao.action.DeleteBuilder;
import pers.zr.opensource.magic.dao.matcher.EqualsMatcher;
import pers.zr.opensource.magic.dao.matcher.LeftLikeMatcher;
import pers.zr.opensource.magic.dao.matcher.Matcher;
import test.pers.zr.magic.dao.core.BaseJunit;

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
        Matcher matcher = new EqualsMatcher("user_id", 999999999);
        Matcher matcher2 = new EqualsMatcher("xxx_id", 999999999);
        delete.addCondition(matcher);
        delete.addCondition(matcher2);
        System.out.println(delete.getSql());
        System.out.println(delete.getParams()[0]);



    }



}
