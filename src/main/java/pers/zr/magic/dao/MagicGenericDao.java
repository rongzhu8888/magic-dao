package pers.zr.magic.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pers.zr.magic.dao.constants.ActionMode;
import pers.zr.magic.dao.matcher.EqualsMatcher;
import pers.zr.magic.dao.matcher.Matcher;
import pers.zr.magic.dao.utils.ClassUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhurong on 2016-4-29.
 */
public abstract class MagicGenericDao<ENTITY extends Serializable> implements MagicDao<ENTITY> {

    protected MagicDataSource dataSource;

    public void setDataSource(MagicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    private RowMapper<ENTITY> entityRowMapper;
    private Class<ENTITY> entityClass;



    @Override
    public ENTITY get(Object... keys) {

//        Type[] types = ClassUtil.getGenericTypes(getClass());
//        entityClass = (Class<ENTITY>)types[0];
//
//        Matcher matcher = new EqualsMatcher();
//        JdbcTemplate jdbcTemplate = dataSource.getJdbcTemplate(ActionMode.QUERY);
//        List<ENTITY> list = jdbcTemplate.

        return null;
    }

    @Override
    public void insert(ENTITY entity){}


    @Override
    public Object insertAndGetKey(ENTITY entity){
        return null;
    }


    @Override
    public void updateByKey(ENTITY entity) {}


    @Override
    public void deleteByKey(Object... keys){}


}
