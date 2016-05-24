package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.opensource.magic.dao.action.ActionBuilderContainer;
import pers.zr.opensource.magic.dao.action.Update;
import pers.zr.opensource.magic.dao.action.UpdateBuilder;
import pers.zr.opensource.magic.dao.matcher.EqualsMatcher;
import test.pers.zr.magic.dao.core.BaseJunit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhurong on 2016-5-5.
 */
public class UpdateTest extends BaseJunit {

    private static UpdateBuilder updateBuilder;

    @BeforeClass
    public static void generateDeleteBuilder() {
        updateBuilder = new UpdateBuilder(table, shardStrategy);
        ActionBuilderContainer.setActionBuilder(updateBuilder);
    }

    @Test
    public void testGetSqlAndParams() {

        Update update = updateBuilder.build();
        update.addCondition(new EqualsMatcher("user_id", 2304820800001L));

        Map<String, Object> updateFields = new HashMap<String, Object>();
        updateFields.put("order_status", 0);
        update.setUpdateFields(updateFields);

        System.out.println(update.getSql());
        System.out.println(update.getParams().length);

    }

}
