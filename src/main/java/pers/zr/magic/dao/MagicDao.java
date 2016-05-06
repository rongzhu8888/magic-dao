package pers.zr.magic.dao;

import java.io.Serializable;

/**
 * Created by zhurong on 2016-4-29.
 */
public interface MagicDao<KEY extends Serializable, ENTITY extends Serializable> {

    public ENTITY get(KEY key);

    public void insert(ENTITY entity);

    public KEY insertAndGetKey(ENTITY entity);

    public void updateByKey(ENTITY entity);

    public void deleteByKey(KEY key);


}
