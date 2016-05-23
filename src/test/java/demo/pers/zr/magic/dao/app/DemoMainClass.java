package demo.pers.zr.magic.dao.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pers.zr.magic.dao.matcher.LeftLikeMatcher;
import pers.zr.magic.dao.matcher.Matcher;

import java.util.List;

/**
 * Created by zhurong on 2016-5-23.
 */
public class DemoMainClass {

    public static void main(String []args) {

        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext-dao.xml");

        AppService service = (AppService) ctx.getBean(AppService.class);

        List<AppEntity> appEntityList = service.queryApps();
        System.out.println(appEntityList.size());




    }
}
