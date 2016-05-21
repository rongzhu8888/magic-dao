package demo.pers.zr.magic.dao.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pers.zr.magic.dao.matcher.*;

import java.util.List;

/**
 * Created by zhurong on 2016-5-6.
 */

public class AppService {



    public static void main(String []args) {

        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext-dao.xml");

        MagicAppDaoImpl dao = (MagicAppDaoImpl) ctx.getBean(MagicAppDaoImpl.class);

        Matcher matcher = new LeftLikeMatcher("app_name", "二维码_");
        List<AppEntity> appEntityList = dao.query(matcher);
        System.out.println(appEntityList.size());

//
//        AppKey key = new AppKey();
//        key.setAppId(2L);
//        //test getByKey
//        dao.get(key);
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("group_id", 2);
//        System.out.println(dao.getCount(map));
//
//        Map<String, Object> valueMap = new HashMap<>();
//        valueMap.put("group_id", 4);
//        dao.update(valueMap, new LessMatcher("update_time", new Date()));

        //test insert
//        AppEntity entity = new AppEntity();
////        entity.setAppId("hdb-laba");
//        entity.setAppDesc("拉霸");
//        entity.setAppName("hdb-laba-");
//        entity.setGroupId("2");
//        entity.setCreateTime(new Date());
//        System.out.println(dao.insertAndGetKey(entity));




//        //test delete
//        dao.delete("hdb-laba-10");
//
//        //test update
//        entity = new AppEntity();
//        entity.setAppId("hdb-laba-11");
//        entity.setAppDesc("更新测试");
//        entity.setAppName("更新测试");
//        entity.setGroupId("3");
//        dao.update(entity);
//
//        //test query
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("group_id", 2);
//        List<AppEntity> list = dao.query(map);
//        System.out.println(list.size());
//
//        Order order = new Order("create_time", OrderType.ASC);
//        Order order2 = new Order("update_time", OrderType.DESC);
//        list = dao.query(map, order, order2);
//        System.out.println(list.size());
//
//        Page pageModel = new Page(1, 20);
//        list = dao.query(map, pageModel);
//        System.out.println(list.size());
//
//        list = dao.query(map, pageModel, order, order2);
//        System.out.println(list.size());


    }
}
