package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.action.ActionBuilderContainer;
import pers.zr.magic.dao.action.Delete;
import pers.zr.magic.dao.action.DeleteBuilder;
import pers.zr.magic.dao.matcher.*;
import test.pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.constants.ConditionType;

/**
 * Created by zhurong on 2016-5-3.
 */
public class DeleteTest extends BaseJunit{

    private static DeleteBuilder deleteBuilder;

    @BeforeClass
    public static void generateDeleteBuilder() {
        deleteBuilder = new DeleteBuilder(table, null);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);
    }


    @Test
    public void testGetSqlAndParams() {
        Delete delete = deleteBuilder.build();
//        delete.addCondition(new EqualsMatcher("app_id", "100001'ASDOFJ\\234"));
//        delete.addCondition(new InMatcher("app_id", new Object[]{100001L, "100002L"}));
        Matcher matcher = new LeftLikeMatcher("app_name", "二维码%");
        delete.addCondition(matcher);
        System.out.println(delete.getSql());
        System.out.println(delete.getParams()[0]);



    }



}
