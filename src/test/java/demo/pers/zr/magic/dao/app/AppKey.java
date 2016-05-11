package demo.pers.zr.magic.dao.app;

import pers.zr.magic.dao.annotation.Key;

import java.io.Serializable;

/**
 * Created by zhurong on 2016-5-11.
 */
public class AppKey implements Serializable {

    @Key(column = "app_id", autoIncrement = true)
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
