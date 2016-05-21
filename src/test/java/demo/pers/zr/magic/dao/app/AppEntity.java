package demo.pers.zr.magic.dao.app;

import pers.zr.magic.dao.annotation.Column;
import pers.zr.magic.dao.annotation.Key;
import pers.zr.magic.dao.annotation.Shard;
import pers.zr.magic.dao.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhurong on 2016-5-6.
 */
@Table(name = "mc_app")
//@Shard(shardColumn = "app_id", shardCount = 32, separator = "_")
public class AppEntity extends AppKey implements Serializable {

    @Column(value = "app_name")
    private String appName;

    @Column(value = "app_code")
    private String appCode;

    @Column(value = "group_id")
    private String groupId;

    @Column(value = "create_time")
    private Date createTime;

    @Column(value = "update_time", readOnly = true)
    private Date updateTime;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
