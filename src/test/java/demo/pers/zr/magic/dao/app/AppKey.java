package demo.pers.zr.magic.dao.app;

import pers.zr.opensource.magic.dao.annotation.Key;

import java.io.Serializable;

/**
 * Created by zhurong on 2016-5-11.
 */
public class AppKey implements Serializable {

    @Key(column = "app_id")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
