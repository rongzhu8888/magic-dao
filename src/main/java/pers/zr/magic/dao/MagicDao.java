package pers.zr.magic.dao;

import java.io.Serializable;

/**
 * Created by zhurong on 2016-4-29.
 */
public interface MagicDao<ENTITY extends Serializable> {

    public ENTITY get(Object... keys);

    public void insert(ENTITY entity);

    public Object insertAndGetKey(ENTITY entity);

    public void updateByKey(ENTITY entity);

    public void deleteByKey(Object... keys);


}
