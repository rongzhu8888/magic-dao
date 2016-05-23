package demo.pers.zr.magic.dao.app;

import pers.zr.magic.dao.annotation.DataSource;
import pers.zr.magic.dao.constants.DataSourceType;
import pers.zr.magic.dao.matcher.*;

import java.util.List;

/**
 * Created by zhurong on 2016-5-6.
 */
@DataSource(type = DataSourceType.MASTER)
public class AppService {

    private MagicAppDao appDao;

    public List<AppEntity> queryApps() {
        Matcher matcher = new LeftLikeMatcher("app_name", "二维码_");
        List<AppEntity> appEntityList = appDao.query(matcher);
        return appEntityList;
    }

    public void setAppDao(MagicAppDao appDao) {
        this.appDao = appDao;
    }
}
