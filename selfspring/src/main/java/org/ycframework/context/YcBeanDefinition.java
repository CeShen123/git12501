package org.ycframework.context;

/*
  对一个Bean的特征的包装的类
  特征：scope(singleton/prototype/...)
  lazy(true/false) 懒加载
  primary:主实例|优先实例
 */

public class YcBeanDefinition {
    private boolean islazy;
    private String scope="Stringleton";
    private boolean isPrimary;

    private String classInfo;//类的实例信息

    public boolean isIslazy() {
        return islazy;
    }

    public void setIslazy(boolean islazy) {
        this.islazy = islazy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }
}
