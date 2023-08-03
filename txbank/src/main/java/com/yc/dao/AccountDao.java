package com.yc.dao;

import com.yc.bean.Account;

import java.util.List;

public interface AccountDao {
/*
  添加Acount账号
  这个新帐号的编号 如何取出  auto_increment自动生成的id号   how to do

 */
    public int insert(double money);

    /*
      根据账号将money更新  都当成:balance=balance+money
      正数表示存 负数表示取
     */
    public void update(int accountid,double money);

    //删除账号
    public void delete(int accountid);

    //查询账号总数
    public int findCount();

    //查询所有的账户
    public List<Account> findAll();

    //根据id查询账号
    public Account finbById(int accountid);
}
