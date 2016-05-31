package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.opensource.magic.dao.action.ActionBuilderContainer;
import pers.zr.opensource.magic.dao.action.Insert;
import pers.zr.opensource.magic.dao.action.InsertBuilder;
import test.pers.zr.magic.dao.core.BaseJunit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhurong on 2016-5-5.
 */
public class InsertTest extends BaseJunit {

    private static InsertBuilder insertBuilder;

    @BeforeClass
    public static void generateBuidler() {
        insertBuilder = new InsertBuilder(table);
        ActionBuilderContainer.setActionBuilder(insertBuilder);
    }

    @Test
    public void testGetSqlAndParams() {

        Insert insert = insertBuilder.build();

        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("order_id", 1111110234234929L);
        fields.put("user_id", 100000010099L);
        fields.put("create_time", new Date());
        fields.put("order_status", 1);

        insert.setInsertFields(fields);

        System.out.println(insert.getSql());
        System.out.println(insert.getParams().length);



    }
}
