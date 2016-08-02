package pers.zr.opensource.magic.dao.annotation;

import java.lang.annotation.*;

/**
 * Created by zhurong on 2016-5-6.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableShard {

    String shardTable();

    String[] shardColumns();

    int shardCount();

    String separator() default "_";
}
