package pers.zr.magic.dao;

import pers.zr.magic.dao.matcher.Matcher;
import pers.zr.magic.dao.order.Order;
import pers.zr.magic.dao.page.PageModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhurong on 2016-4-29.
 */
public interface MagicDao<KEY extends Serializable, ENTITY extends Serializable> {

    public ENTITY get(KEY key);

    public void insert(ENTITY entity);

    public Long insertAndGetKey(ENTITY entity);

    public void update(ENTITY entity);

    public void delete(KEY key);

    public List<ENTITY> query(Map<String, Object> conditions, Order... orders);

    public List<ENTITY> query(Map<String, Object> conditions, PageModel pageModel, Order... orders);

    public Long getCount(Map<String, Object> conditions);

    public void delete(Matcher...conditions);

    public void update(Map<String, Object> valueMap, Matcher...conditions);


}
