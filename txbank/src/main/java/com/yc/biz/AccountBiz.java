package com.yc.biz;

import com.yc.bean.Account;

/**
 * @program: mavenProject
 * @description:
 * @author: Joker
 * @create: 2023-08-02 14:15
 */
public interface AccountBiz {
    public void addAccount(int accountid, double money);

    /**
     * 开户操作
     *
     * @param money
     * @return
     */
    public Account openAccount(double money);

    /**
     * 存款操作
     *
     * @param accountid
     * @param money
     * @return
     */
    public Account deposite(int accountid, double money);

    /**
     * 存款实际操作
     *
     * @param accountid
     * @param money
     * @param transferId
     * @return
     */
    public Account deposite(int accountid, double money, Integer transferId);

    /**
     * 取款操作
     *
     * @param accountid
     * @param money
     * @return
     */
    public Account withdraw(int accountid, double money);

    /**
     * 取款实际操作
     *
     * @param accountid
     * @param money
     * @param transferId
     * @return
     */
    public Account withdraw(int accountid, double money, Integer transferId);

    /**
     * 转账操作
     *
     * @param accounId
     * @param money
     * @param toAccountId
     * @return
     */
    public Account transfer(int accounId, double money, int toAccountId);

    /**
     * 通过编号查询
     *
     * @param accountid
     * @return
     */
    public Account findAccount(int accountid);
}