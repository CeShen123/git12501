package com.yc.biz;

import com.yc.bean.Account;
import com.yc.bean.OpRecord;
import com.yc.bean.OpType;
import com.yc.dao.AccountDao;
import com.yc.dao.OpRecordDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: mavenProject
 * @description:
 * @author: Joker
 * @create: 2023-08-02 14:15
 */
@Service
@Log4j2
@Transactional
public class AccountBizImpl implements AccountBiz {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private OpRecordDao opRecordDao;

    @Override
    public void addAccount(int accountid, double money) {
        System.out.println("添加账户:" + accountid);
    }

    @Override
    public Account openAccount(double money) {
        //开户操作 返回新账号的 id
        int accountid = this.accountDao.insert(money);
        //包装日志信息
        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid(accountid);
        opRecord.setOpmoney(money);
        opRecord.setOpType(OpType.DEPOSITE);
        this.opRecordDao.insertOpRecord(opRecord);
        //返回新账号的信息
        Account  a = new Account();
        a.setAccountid(accountid);
        a.setMoney(money);
        return a;
    }

    @Override
    public Account deposite(int accountid, double money) {
        return this.deposite(accountid,money,null);
    }

    @Override
    public Account deposite(int accountid, double money, Integer transferId) {
        Account a = null;
        try {
            a = this.accountDao.finbById(accountid);
        }catch (RuntimeException re){
            log.error(re.getMessage());
            throw new RuntimeException("查无此账户" + accountid + "无法完成存款操作");
        }
        //存款时 金额累加
        a.setMoney(a.getMoney() + money);

        this.accountDao.update(accountid,a.getMoney());
        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid(accountid);
        opRecord.setOpmoney(money);
        if(transferId != null){
            opRecord.setOpType(OpType.TRANSFER);
            opRecord.setTransferid(transferId);
        }else {
            opRecord.setOpType(OpType.DEPOSITE);
        }
        this.opRecordDao.insertOpRecord(opRecord);
        return a;
    }

    @Override
    public Account withdraw(int accountid, double money) {
        return this.withdraw(accountid,money,null);
    }

    @Override
    public Account withdraw(int accountid, double money, Integer transferId) {
        Account a = null;
        try {
            a = this.accountDao.finbById(accountid);
        }catch (RuntimeException re){
            log.error(re.getMessage());
            throw new RuntimeException("查无此账户" + accountid + "无法完成取款操作");
        }
        //取款时 金额相减
        a.setMoney(a.getMoney() - money);

        OpRecord opRecord = new OpRecord();
        opRecord.setAccountid(accountid);
        opRecord.setOpmoney(money);
        if(transferId != null){
            opRecord.setOpType(OpType.TRANSFER);
            opRecord.setTransferid(transferId);
        }else {
            opRecord.setOpType(OpType.WITHDRAW);
        }
        this.opRecordDao.insertOpRecord(opRecord); //先插入日志

        this.accountDao.update(accountid,a.getMoney()); //再减金额
        return a;
    }

    @Override
    public Account transfer(int accounId, double money, int toAccountId) {
        this.deposite(toAccountId,money,accounId); //收款方
        Account a = this.withdraw(accounId,money,toAccountId);
        return a;
    }

    @Override
    public Account findAccount(int accountid) {
        return this.accountDao.finbById(accountid);
    }
}
