package com.yc.CglibProxy;

public class Test3 {
    public static void main(String[] args) {
        CglibProxyTooll jpt=new CglibProxyTooll(new OrderBizImpl());
        OrderBizImpl ob = (OrderBizImpl) jpt.createProxy();
        System.out.println("生成代理对象:"+ob.toString());

        ob.findOrder();

        ob.addOrder(1,99);
    }
}
