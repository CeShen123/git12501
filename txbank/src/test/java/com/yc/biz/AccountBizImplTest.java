package com.yc.biz;

import com.yc.bean.Account;
import com.yc.configs.DataSourceConfig;
import junit.framework.TestCase;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class, DataSourceConfig.class})
@Log4j2
public class AccountBizImplTest extends TestCase {

    @Autowired
    private AccountBiz accountBiz;

    @Test
    public void findAccount() {
        Account a = accountBiz.findAccount(1);
        Assert.assertNotNull(a);
        log.info(a);
    }

    @Test
    public void openAccount() {
        Account a = accountBiz.openAccount(100);
        Assert.assertNotNull(a);
        log.info(a);
    }

    @Test
    public void deposite() {
        Account a = accountBiz.deposite(1,10);
        Assert.assertNotNull(a);
        log.info(a);
    }


    @Test
    public void withdraw() {
        Account a = accountBiz.withdraw(7,1);
        Assert.assertNotNull(a);
        log.info(a);
    }


    @Test
    public void transfer() {
        Account a = accountBiz.transfer(1,100,4);
        Assert.assertNotNull(a);
        log.info(a);
    }


}
