package pers.zr.magic.dao.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pers.zr.magic.dao.MagicMultiDataSource;
import pers.zr.magic.dao.annotation.DataSource;
import pers.zr.magic.dao.constants.DataSourceType;

import java.lang.reflect.Method;

/**
 *
 * 默认情况下主从分离环境：读从库，写主库。但是某些对实时性要求比较高的场景，
 * 需要读主库。MagicRuntimeDataSourceAop设计便是为了解决此种特殊情况。
 * Created by zhurong on 2016-5-23.
 */
public class ReadingDataSourceAop {

    private Logger log = LogManager.getLogger(ReadingDataSourceAop.class);

    public Object determine(ProceedingJoinPoint pjp) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Class<?> clazz = method.getDeclaringClass();

            DataSource dataSourceAnnotation = method.getAnnotation(DataSource.class);
            if(null == dataSourceAnnotation) {
                dataSourceAnnotation = clazz.getAnnotation(DataSource.class);
            }
            DataSourceType currentType = (null != dataSourceAnnotation) ? dataSourceAnnotation.type() : DataSourceType.SLAVE;
            //set 当前线程运行时的ReadingDataSourceType
            MagicMultiDataSource.runtimeReadingDataSourceType.set(currentType);
            if(log.isDebugEnabled()) {
                log.debug("invoke service=" + clazz.getName() + "." + method.getName() + ", readingDataSource=" +  currentType);
            }
            return pjp.proceed();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            //remove 当前线程运行时的ReadingDataSourceType
            MagicMultiDataSource.runtimeReadingDataSourceType.remove();

        }
    }

}
