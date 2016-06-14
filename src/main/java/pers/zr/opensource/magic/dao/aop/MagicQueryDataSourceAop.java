package pers.zr.opensource.magic.dao.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pers.zr.opensource.magic.dao.MagicMultiDataSource;
import pers.zr.opensource.magic.dao.annotation.QueryDataSource;
import pers.zr.opensource.magic.dao.constants.DataSourceType;

import java.lang.reflect.Method;

/**
 *
 * Aop to analysis @QueryDataSource annotation, and determine the reading dataSource type: master or slave
 * Created by zhurong on 2016-5-23.
 */
public class MagicQueryDataSourceAop {

    private Log log = LogFactory.getLog(MagicQueryDataSourceAop.class);

    public Object determine(ProceedingJoinPoint pjp) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Class<?> clazz = method.getDeclaringClass();

            QueryDataSource queryDataSourceAnnotation = method.getAnnotation(QueryDataSource.class);
            if(null == queryDataSourceAnnotation) {
                queryDataSourceAnnotation = clazz.getAnnotation(QueryDataSource.class);
            }
            DataSourceType currentType = (null != queryDataSourceAnnotation) ? queryDataSourceAnnotation.type() : DataSourceType.SLAVE;
            //set current thread reading dataSource type
            MagicMultiDataSource.currentThreadQueryDataSourceType.set(currentType);
            if(log.isDebugEnabled()) {
                log.debug("invoke service=" + clazz.getName() + "." + method.getName() + ", readingDataSource=" +  currentType);
            }
            return pjp.proceed();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            //remove current thread reading dataSource type
            MagicMultiDataSource.currentThreadQueryDataSourceType.remove();

        }
    }

}
