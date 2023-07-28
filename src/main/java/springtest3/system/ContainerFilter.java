package springtest3.system;

public interface ContainerFilter {
    /*
    判断此对象是否为有效对象
    obj
     */
    public boolean doFilter(Object obj);
}
