package pers.zr.magic.dao.annotation;

import java.lang.annotation.*;

/**
 * Created by zhurong on 2016-5-6.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Shard {

    int shardCount();

    String shardColumn();

    String separator() default "";
}
