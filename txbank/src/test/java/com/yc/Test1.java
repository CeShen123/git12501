package com.yc;

import com.yc.biz.AccountBiz;
import com.yc.biz.Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//ApplicationContext ac =new AnnotationConfigApplicationContext(Config.class);
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class Test1 {
    //也可以在这里完成DI
    @Autowired
    private AccountBiz accountBiz;

    //单元测试用例
    @Test
    public void testAddAccount(){
        accountBiz.addAccount(1,99);
    }

    //引入断言
    public void testAdd(){
        int x=3,y=4;
        Assert.assertEquals(7,x+y);
    }
}
