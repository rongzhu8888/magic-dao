package test.pers.zr.magic.dao.core.action;

import org.junit.BeforeClass;
import org.junit.Test;
import pers.zr.magic.dao.action.ActionBuilderContainer;
import pers.zr.magic.dao.action.Insert;
import pers.zr.magic.dao.action.InsertBuilder;
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
        fields.put("prize_id", 1111);
        fields.put("prize_name", "test");
        fields.put("create_time", new Date());

        insert.setInsertFields(fields);

        System.out.println(insert.getSql());
        System.out.println(insert.getParams().length);



    }
}
