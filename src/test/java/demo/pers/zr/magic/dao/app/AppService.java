package demo.pers.zr.magic.dao.app;

import pers.zr.opensource.magic.dao.annotation.DataSource;
import pers.zr.opensource.magic.dao.constants.DataSourceType;
import pers.zr.opensource.magic.dao.matcher.LeftLikeMatcher;
import pers.zr.opensource.magic.dao.matcher.Matcher;

import java.util.List;

/**
 * Created by zhurong on 2016-5-6.
 */
@DataSource(type = DataSourceType.MASTER)
public class AppService {

    private MagicAppDao appDao;

    public List<AppEntity> queryApps() {
        Matcher matcher = new LeftLikeMatcher("app_id", "hdb-core");
        List<AppEntity> appEntityList = appDao.query(matcher);
        return appEntityList;
    }


    public void setAppDao(MagicAppDao appDao) {
        this.appDao = appDao;
    }
}
