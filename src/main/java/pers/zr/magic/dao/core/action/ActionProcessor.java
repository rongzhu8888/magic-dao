package pers.zr.magic.dao.core.action;

import pers.zr.magic.dao.core.MagicDataSource;

import java.util.List;

/**
 * Created by zhurong on 2016-4-28.
 */
public interface ActionProcessor<T> {

    ActionResult execute(MagicDataSource dataSource,  T action);

    ActionResult[] batchExecute(MagicDataSource dataSource, List<T> actionList);

}
