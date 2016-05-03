package pers.zr.magic.dao.core.action;

import org.junit.Test;
import pers.zr.magic.dao.core.BaseJunit;
import pers.zr.magic.dao.core.constants.ActionMode;
import pers.zr.magic.dao.core.constants.ConditionType;
import pers.zr.magic.dao.core.matcher.EqualsMatcher;

import static org.junit.Assert.*;

/**
 * Created by peter.zhu on 2016/4/30.
 */
public class ActionBuilderTest extends BaseJunit{

    @Test
    public void testDeleteBuilder() {

        DeleteBuilder deleteBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.DELETE);
        assertNull(deleteBuilder);

        deleteBuilder = new DeleteBuilder();
        deleteBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(deleteBuilder);

        DeleteBuilder deleteBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.DELETE);
        assertNotNull(deleteBuilder2);

        assertEquals(deleteBuilder, deleteBuilder2);

        Delete delete = deleteBuilder.build();
        assertNotNull(delete);


    }

    @Test
    public void  testInsertBuilder() {

        InsertBuilder insertBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.INSERT);
        assertNull(insertBuilder);

        insertBuilder = new InsertBuilder();
        insertBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(insertBuilder);

        InsertBuilder insertBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.INSERT);
        assertNotNull(insertBuilder2);

        assertEquals(insertBuilder, insertBuilder2);

        Insert insert = insertBuilder.build();

        assertNotNull(insert);


    }

    @Test
    public void testUpdateBuilder() {

        UpdateBuilder updateBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.UPDATE);
        assertNull(updateBuilder);

        updateBuilder = new UpdateBuilder();
        updateBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(updateBuilder);

        UpdateBuilder updateBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.UPDATE);
        assertNotNull(updateBuilder2);

        assertEquals(updateBuilder, updateBuilder2);

        Update update = updateBuilder.build();
        assertNotNull(update);


    }

    @Test
    public void testQueryBuilder() {

        QueryBuilder queryBuilder = ActionBuilderContainer.getActionBuilder(table, ActionMode.QUERY);
        assertNull(queryBuilder);

        queryBuilder = new QueryBuilder();
        queryBuilder.setTable(table).setShardStrategy(shardStrategy);
        ActionBuilderContainer.setActionBuilder(queryBuilder);

        QueryBuilder queryBuilder2 = ActionBuilderContainer.getActionBuilder(table, ActionMode.QUERY);
        assertNotNull(queryBuilder2);

        assertEquals(queryBuilder, queryBuilder2);

        Query query = queryBuilder.build();
        assertNotNull(query);

        Query query2 = queryBuilder.build();
        assertNotNull(query2);

        assertNotEquals(query, query2);

    }



}
