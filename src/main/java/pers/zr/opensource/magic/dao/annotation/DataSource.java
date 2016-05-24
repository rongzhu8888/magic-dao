package pers.zr.opensource.magic.dao.annotation;

import pers.zr.opensource.magic.dao.constants.DataSourceType;

import java.lang.annotation.*;

/**
 * Created by zhurong on 2016-5-5.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    DataSourceType type();

}
