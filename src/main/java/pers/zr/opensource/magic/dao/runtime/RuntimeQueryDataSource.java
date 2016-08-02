package pers.zr.opensource.magic.dao.runtime;

import pers.zr.opensource.magic.dao.constants.DataSourceType;

/**
 * Created by zhurong on 2016-8-2.
 */
public class RuntimeQueryDataSource {

    public static  ThreadLocal<String> alias = new ThreadLocal<String>();

    public static ThreadLocal<DataSourceType> type = new ThreadLocal<DataSourceType>();


}
