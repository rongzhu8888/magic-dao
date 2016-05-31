package pers.zr.opensource.magic.dao.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pers.zr.opensource.magic.dao.MagicMultiDataSource;
import pers.zr.opensource.magic.dao.annotation.DataSource;
import pers.zr.opensource.magic.dao.constants.DataSourceType;

import java.lang.reflect.Method;

/**
 *
 * Aop to analysis @DataSource annotation, and determine the reading dataSource type: master or slave
 * Created by zhurong on 2016-5-23.
 */
public class ReadingDataSourceAop {

    private Log log = LogFactory.getLog(ReadingDataSourceAop.class);

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
            //set current thread reading dataSource type
            MagicMultiDataSource.runtimeReadingDataSourceType.set(currentType);
            if(log.isDebugEnabled()) {
                log.debug("invoke service=" + clazz.getName() + "." + method.getName() + ", readingDataSource=" +  currentType);
            }
            return pjp.proceed();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            //remove current thread reading dataSource type
            MagicMultiDataSource.runtimeReadingDataSourceType.remove();

        }
    }

}
