package demo.pers.zr.magic.dao.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhurong on 2016-5-6.
 */

public class AppService {



    public static void main(String []args) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext-dao.xml");

        MagicAppDaoImpl dao = (MagicAppDaoImpl) ctx.getBean(MagicAppDaoImpl.class);
        dao.get("hdb-social");

    }
}
