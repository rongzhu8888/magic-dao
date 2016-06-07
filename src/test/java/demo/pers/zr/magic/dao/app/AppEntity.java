package demo.pers.zr.magic.dao.app;

import pers.zr.opensource.magic.dao.annotation.Column;
import pers.zr.opensource.magic.dao.annotation.Table;
import pers.zr.opensource.magic.dao.annotation.TableShard;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhurong on 2016-5-6.
 */
@Table(name = "mc_app")
//@TableShard(shardTable = "mc_app", shardColumn = "app_id", shardCount = 32, separator = "_")
public class AppEntity extends AppKey implements Serializable {

    @Column(value = "app_name")
    private String appName;

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
