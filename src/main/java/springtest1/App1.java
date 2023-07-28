package springtest1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springtest1.Config;
import springtest1.biz.UserBiz;
import springtest1.dao.UserDao;

public class App1 {
    public static void main(String[] args) {
        //首先创建容器 
        //classPathApplicationContext  类路径下有一个xml文件来配置  来生成容器
        //FileSystemXmlApplicationContext //FileSystem任意路径(c:/...)文件系统路程下的xml配置文件 来生成容器
       // AnnotationConfigApplicationContext  读取注解的类(@configuration) 生成容器
        ApplicationContext container =new AnnotationConfigApplicationContext(Config.class);
        //容器中已经建好了这个类  "键:userDaoImpl"  值:是对象
        UserDao ud= (UserDao) container.getBean("userDaoImpl");//bean id的约定是：类名首字母小写
        ud.add("王五");

        //取业务层的类
        UserBiz ub= (UserBiz) container.getBean("userBizImpl");
        ub.add("王五");
    }
}
