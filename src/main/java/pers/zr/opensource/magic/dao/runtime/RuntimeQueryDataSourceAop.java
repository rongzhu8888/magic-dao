package pers.zr.opensource.magic.dao.runtime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pers.zr.opensource.magic.dao.annotation.QueryDataSource;
import pers.zr.opensource.magic.dao.constants.DataSourceType;

import java.lang.reflect.Method;

/**
 *
 * Aop to analysis @QueryDataSource annotation, and determine the reading dataSource type: master or slave
 * Created by zhurong on 2016-5-23.
 */
public class RuntimeQueryDataSourceAop {

    private Log log = LogFactory.getLog(RuntimeQueryDataSourceAop.class);

    public Object determine(ProceedingJoinPoint pjp) {
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Class<?> clazz = method.getDeclaringClass();

            QueryDataSource queryDataSourceAnnotation = method.getAnnotation(QueryDataSource.class);
            if(null == queryDataSourceAnnotation) {
                queryDataSourceAnnotation = clazz.getAnnotation(QueryDataSource.class);
            }

            //set alias of  current thread query datasource
            String currentDataSourceAlias = queryDataSourceAnnotation.alias();
            RuntimeQueryDataSource.alias.set(currentDataSourceAlias);

            if(currentDataSourceAlias == null) {
                //set type of current thread query dataSource
                DataSourceType currentDataSourceType = (null != queryDataSourceAnnotation) ? queryDataSourceAnnotation.type() : DataSourceType.SLAVE;
                RuntimeQueryDataSource.type.set(currentDataSourceType);
                if(log.isDebugEnabled()) {
                    log.debug("invoke service=" + clazz.getName() + "." + method.getName() + ", QueryDataSource=[type:" + currentDataSourceType + "]");
                }
            }else {
                if(log.isDebugEnabled()) {
                    log.debug("invoke service=" + clazz.getName() + "." + method.getName() + ", QueryDataSource=[alias:" + currentDataSourceAlias + "]");
                }
            }
            return pjp.proceed();

        } catch (Throwable t) {
            throw new RuntimeException(t);

        } finally {
            //clear current thread query datasource
            RuntimeQueryDataSource.alias.remove();
            RuntimeQueryDataSource.type.remove();

        }
    }

}
