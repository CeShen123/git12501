package com.yc;

import org.ycframework.annotation.YcResource;
import org.ycframework.annotation.YcService;

@YcService
public class UserBizImpl implements UserBiz{

    @YcResource(name = "userDaoImpl")
    private UserDao userDao;

    @Override
    public void add(String uname) {
        userDao.add(uname);
    }
}
