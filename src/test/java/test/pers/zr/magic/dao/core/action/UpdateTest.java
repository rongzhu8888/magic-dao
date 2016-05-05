package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.action.ActionBuilderContainer;
import pers.zr.magic.dao.action.Update;
import pers.zr.magic.dao.action.UpdateBuilder;
import pers.zr.magic.dao.constants.ConditionType;
import pers.zr.magic.dao.matcher.LikeMatcher;
import test.pers.zr.magic.dao.core.BaseJunit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhurong on 2016-5-5.
 */
public class UpdateTest extends BaseJunit {

    private static UpdateBuilder updateBuilder;

    @BeforeClass
    public static void generateDeleteBuilder() {
        updateBuilder = new UpdateBuilder(table);
        ActionBuilderContainer.setActionBuilder(updateBuilder);
    }

    @Test
    public void testGetSqlAndParams() {

        Update update = updateBuilder.build();
        update.addCondition(new LikeMatcher("prize_name", "_ttt"), ConditionType.AND);

        Map<String, Object> updateFields = new HashMap<String, Object>();
        updateFields.put("prize_size", 1000);
        updateFields.put("update_time", new Date());
        update.setUpdateFields(updateFields);

        System.out.println(update.getSql());
        System.out.println(update.getParams().length);

    }

}
