package springtest1.dao;

import org.springframework.stereotype.Repository;
import springtest1.dao.UserDao;

@Repository //这是一个dao层的类 由spring托管
            //@component    由spring托管
public class UserDaoImpl implements UserDao {
    public UserDaoImpl(){System.out.println("UserDaoImpl类的构造...");}


    @Override
    public void add(String uname) {
        System.out.println("添加了"+uname);
    }
}
