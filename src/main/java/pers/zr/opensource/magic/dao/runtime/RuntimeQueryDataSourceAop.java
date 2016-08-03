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
            QueryDataSource queryDataSourceAnnotation = null;

            Object target = pjp.getTarget();
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

            //method in class
            Method methodImpl = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            //method in interface
            Method methodInterface = methodSignature.getMethod();

            //current class
            Class<?> clazzImpl = pjp.getTarget().getClass();
            //current interface
            Class<?> clazzInterface = methodInterface.getDeclaringClass();

            //get annotation of method
            queryDataSourceAnnotation = methodImpl.getAnnotation(QueryDataSource.class) != null
                    ? methodImpl.getAnnotation(QueryDataSource.class)
                    : methodInterface.getAnnotation(QueryDataSource.class);


            if(null == queryDataSourceAnnotation) {
                //get annotation of class or interface
                queryDataSourceAnnotation = clazzImpl.getAnnotation(QueryDataSource.class) != null
                        ? clazzImpl.getAnnotation(QueryDataSource.class)
                        : clazzInterface.getAnnotation(QueryDataSource.class);

            }


            //set alias of  current thread query datasource
            String currentDataSourceAlias = queryDataSourceAnnotation.alias();
            if(null == currentDataSourceAlias || "".equals(currentDataSourceAlias)) {
                //set type of current thread query dataSource
                DataSourceType currentDataSourceType = (null != queryDataSourceAnnotation) ? queryDataSourceAnnotation.type() : DataSourceType.SLAVE;
                System.out.println(currentDataSourceType.toString());
                RuntimeQueryDataSource.type.set(currentDataSourceType);
                if(log.isDebugEnabled()) {
                    log.debug("invoke service=" + clazzImpl.getName() + "." + methodImpl.getName() + ", QueryDataSource=[type:" + currentDataSourceType + "]");
                }
            }else {
                RuntimeQueryDataSource.alias.set(currentDataSourceAlias);
                if(log.isDebugEnabled()) {
                    log.debug("invoke service=" + clazzImpl.getName() + "." + methodImpl.getName() + ", QueryDataSource=[alias:" + currentDataSourceAlias + "]");
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
