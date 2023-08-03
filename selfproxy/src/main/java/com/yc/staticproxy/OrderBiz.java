package com.yc.staticproxy;

//抽象主题
public interface OrderBiz {
   void addOrder(int pid,int num);

   void findOrder();
}
