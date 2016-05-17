package test.pers.zr.magic.dao.core.action;

import org.junit.Assert;
import org.junit.Test;
import pers.zr.magic.dao.action.*;
import test.pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.constants.ActionMode;

import static org.junit.Assert.*;

/**
 * Created by zhurong on 2016/4/30.
 */
public class ActionBuilderTest extends BaseJunit{

    @Test
    public void testDeleteBuilder() {

        DeleteBuilder deleteBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.DELETE);
        assertNull(deleteBuilder);

        deleteBuilder = new DeleteBuilder(table);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);

        DeleteBuilder deleteBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.DELETE);
        assertNotNull(deleteBuilder2);

        Assert.assertEquals(deleteBuilder, deleteBuilder2);

        Delete delete = deleteBuilder.build();
        assertNotNull(delete);


    }

    @Test
    public void  testInsertBuilder() {

        InsertBuilder insertBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.INSERT);
        assertNull(insertBuilder);

        insertBuilder = new InsertBuilder(table);
        ActionBuilderContainer.setActionBuilder(insertBuilder);

        InsertBuilder insertBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.INSERT);
        assertNotNull(insertBuilder2);

        Assert.assertEquals(insertBuilder, insertBuilder2);

        Insert insert = insertBuilder.build();

        assertNotNull(insert);


    }

    @Test
    public void testUpdateBuilder() {

        UpdateBuilder updateBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.UPDATE);
        assertNull(updateBuilder);

        updateBuilder = new UpdateBuilder(table);
        ActionBuilderContainer.setActionBuilder(updateBuilder);

        UpdateBuilder updateBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.UPDATE);
        assertNotNull(updateBuilder2);

        Assert.assertEquals(updateBuilder, updateBuilder2);

        Update update = updateBuilder.build();
        assertNotNull(update);


    }

    @Test
    public void testQueryBuilder() {

        QueryBuilder queryBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.QUERY);
        assertNull(queryBuilder);

        queryBuilder = new QueryBuilder(table);
        ActionBuilderContainer.setActionBuilder(queryBuilder);

        QueryBuilder queryBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.QUERY);
        assertNotNull(queryBuilder2);

        Assert.assertEquals(queryBuilder, queryBuilder2);

        Query query = queryBuilder.build();
        assertNotNull(query);

        Query query2 = queryBuilder.build();
        assertNotNull(query2);

        Assert.assertNotEquals(query, query2);

    }



}
