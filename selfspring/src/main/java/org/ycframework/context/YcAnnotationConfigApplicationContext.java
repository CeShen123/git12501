package org.ycframework.context;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ycframework.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class YcAnnotationConfigApplicationContext implements YcApplicationContext{
    private Logger logger = LoggerFactory.getLogger(YcAnnotationConfigApplicationContext.class);
    //存每个 待托管的Bean的定义信息
    private Map<String,YcBeanDefinition> beanDefinitionMap =new HashMap<>();
    //存每个  实例化后的bean
    private Map<String ,Object> beanMap=new HashMap<>();
    //存系统属性 db.properties
    private Properties pros;

    public YcAnnotationConfigApplicationContext(Class... configClasses) {
        try {
            //.读取系统的属性 存好
            pros = System.getProperties();
            List<String> toScanPackagePath = new ArrayList<>();
            for (Class cls : configClasses) {
                if (cls.isAnnotationPresent(YcConfiguration.class) == false) {
                    continue;
                }
                String[] basePackages = null;
                //扫描配置类上的  @YcComponentScan注释 读取要扫描的包
                if (cls.isAnnotationPresent(YcComponentScan.class)) {
                    //如果 则说明此配置类上有@YcComponentScan 则读取backPackages
                    YcComponentScan ycComponentScan = (YcComponentScan) cls.getAnnotation(YcComponentScan.class);
                    basePackages = ycComponentScan.basePackages();
                    if (basePackages == null || basePackages.length < 0) {
                        basePackages = new String[1];
                        basePackages[0] = cls.getPackage().getName();
                    }
                    logger.info(cls.getName() + "类上有@YcComponentScan注解 它要扫描的路径:" + basePackages[0]);
                }
                //开始扫描这些basepackages包下的bean 并加载包装成  BeanDefinition对象  存到beanDefinitionMap
                recursiveLoadBeanDefinition(basePackages);
            }
            //循环  beanDefinitionMap,创建bean(是否有懒加载，是单例)，存到beanMap
            createBean();
            //循环所有托管的beanMap中的bean,看属性和方法上是否有@Autowired @Resource,@Value...,考虑DI
            doDi();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }
    }
    //开始扫描这些basePackages包下的bean 并加载包装成  BeanDefinition 对象，存到beanDefinitionMap
    private void doDi() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //循环的是  beanMap 这是托管Bean
        for (Map.Entry<String,Object> enty:beanMap.entrySet()){
            String beanId=enty.getKey();
            Object beanObj=enty.getValue();
            //TODO:情况二：方法上有@YcResource注解的情况
            //TODO:情况三：构造方法上有@YcResource注解的情况
            Field[] fields=beanObj.getClass().getDeclaredFields();
            for (Field field:fields){
                if (field.isAnnotationPresent(YcResource.class)){
                    YcResource ycResource=field.getAnnotation(YcResource.class);
                    String toDiBeanId=ycResource.name();
                    //从 beanMap中找 是否singleton 是否lazy
                    Object obj=getToDiBean(toDiBeanId);
                    //注入
                    field.setAccessible(true);//因为属性是private 所以要将它 accessible设为true
                    field.set(beanObj,obj);//userBizImpl.userDao=userDaoImpl
                }
            }
        }
    }

    //从baenMap中找，是否singleton 是否lazy
    private Object getToDiBean(String toDiBeanId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      if (beanMap.containsKey(toDiBeanId)){
          return beanMap.get(toDiBeanId);
      }
      //判断beanMap中没有bean是因为lazy
        if (!   beanDefinitionMap.containsKey(toDiBeanId)){
            throw  new RuntimeException("spring容器中没有加载此class"+toDiBeanId);

        }
        YcBeanDefinition bd=beanDefinitionMap.get(toDiBeanId);
        if (bd.isIslazy()){
            //是因为懒，所以没有托管
            String classpath = bd.getClassInfo();
            Object beanObj =Class.forName(classpath).newInstance();
            beanMap.put(toDiBeanId,beanObj);
            return beanObj;
        }
        //是否因为prototype
        if (bd.getScope().equalsIgnoreCase("prototype")){
            //是因为懒 所以没有托管
            String classpath =bd.getClassInfo();
            Object beanObj=Class.forName(classpath).newInstance();
            beanMap.put(toDiBeanId,beanObj);  //原型模式下 每次getBean创建一次bean 所以beanMap不存
            return beanObj;
        }
        return null;
    }

    //循环  beanDefinitionMap,创建bean(是否有懒加载，是单例)，存到beanMap
    private void createBean() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Map.Entry<String,YcBeanDefinition> entry : beanDefinitionMap.entrySet()){
            String beanId=entry.getKey();
            YcBeanDefinition ydb=entry.getValue();
            if (!ydb.isIslazy() && !ydb.getScope().equalsIgnoreCase("prototype")){
                String classInfo=ydb.getClassInfo();
                Object obj=Class.forName(classInfo).newInstance();
                beanMap.put(beanId,obj);
                logger.trace("spring托管了:"+beanId+"=>"+classInfo);
            }

        }
    }

    //开始扫描这些basePackages包下的bean 并加载包装成  BeanDefinition 对象，存到beanDefinitionMap
    private void recursiveLoadBeanDefinition(String[] basePackages){
        for (String basePackage:basePackages){
            //将包名中的 . 替换成 路径中的  /
            String packagePath =basePackage.replaceAll("\\.","/");
            //target/classes  /com/yc
            //Enumeration 集合  URL:每个资源的路径
            Enumeration<URL> files =null;
            try {
                files=Thread.currentThread().getContextClassLoader().getResources(packagePath);
                //循环这个files，看是否是我加载的资源
                while (files.hasMoreElements() ){
                    URL url=files.nextElement();
                    logger.trace("当前递归加载："+url.getFile());
                    //查找此包下的类   com/yc全路径    com/yc包名
                    findPackageClasses(url.getFile(),basePackage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findPackageClasses(String packagePath, String basePackage) {
        //路径异常的处理，前面有/，则去掉它
        if (packagePath.startsWith("/")){
            packagePath=packagePath.substring(1);
        }
        //取这个路径下所有的字节码文件(因为目录下有可能有其他的资源)
        File file=new File(packagePath);
        //写法二：lambda写法
        File[] classFiles =file.listFiles(  (pathname) -> {
            if (pathname.getName().endsWith(".class") ||  pathname.isDirectory()){
                return  true;
            }
            return false;
        });
        //循环此classFiles
        if (classFiles==null || classFiles.length<=0){
            return;
        }
        for (File cf :classFiles){
            if (cf.isDirectory()){
                //继续递归
                logger.trace("递归："+cf.getAbsolutePath()+",它对应的包名:"+(basePackage +","+cf.getName()));
                findPackageClasses(cf.getAbsolutePath(),basePackage+"."+cf.getName());
            }else {
                //是classes文件，则取出文件，判断此文件对应的class中是否有  @Component注解
                URLClassLoader uc=new URLClassLoader(new URL[]{});
                //                                UserDaoImpl.class
                Class cls=null;
                try {
                    cls=uc.loadClass(basePackage+"."+cf.getName().replaceAll(".class",""));
                    //可以支持@Component的子注解
                    if (cls.isAnnotationPresent(YcComponent.class)
                            ||cls.isAnnotationPresent(YcController.class)
                            ||cls.isAnnotationPresent(YcConfiguration.class)
                            ||cls.isAnnotationPresent(YcRepository.class)
                            ||cls.isAnnotationPresent(YcService.class)){
                            logger.info("加载到一个待托管的类:"+cls.getName());
                            YcBeanDefinition bd=new YcBeanDefinition();
                            if (cls.isAnnotationPresent(YcLazy.class)){
                                YcScope ycScope= (YcScope) cls.getAnnotation(YcScope.class);
                                String scope =ycScope.value();
                                bd.setScope(scope);
                            }
                            //实例化 cls.newInstance
                            bd.setClassInfo(basePackage+"."+cf.getName().replaceAll(".class",""));
                            //存到beanDefinitionMap "beanid" ->”beanDefinitionMap“
                            String beanId=getBeanId(cls);
                            this.beanDefinitionMap.put(beanId,bd);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String getBeanId(Class cls) {
        YcComponent ycComponent= (YcComponent) cls.getAnnotation(YcComponent.class);
        YcService ycService= (YcService) cls.getAnnotation(YcService.class);
        YcRepository ycRepository= (YcRepository) cls.getAnnotation(YcRepository.class);
        YcController ycController= (YcController) cls.getAnnotation(YcController.class);

        YcConfiguration ycConfiguration= (YcConfiguration) cls.getAnnotation(YcConfiguration.class);

        if (ycConfiguration!=null){
            return cls.getSimpleName();
        }
        String beanId=null;
        if (ycComponent!=null){
            beanId =ycComponent.value();
        } else if (ycController!=null){
            beanId=ycController.value();
        }else if (ycRepository !=null){
            beanId=ycRepository.value();
        }else if (ycService !=null){
            beanId=ycService.value();
        }
        if (beanId==null ||"".equalsIgnoreCase(beanId)){
            String typename =cls.getSimpleName();
            logger.info("typename为:"+typename);
            beanId =typename.substring(0,1).toLowerCase()+typename.substring(1);
        }
        return beanId;
    }

    @Override
    public Object getBean(String beanid) {
       YcBeanDefinition bd =this.beanDefinitionMap.get(beanid);
       if (bd==null){
           throw  new RuntimeException("容器中没有加载此bean");
       }
       String scope=bd.getScope();
       if ("prototype".equalsIgnoreCase(scope)){
           //原型模式 每次getBaen创建
           Object obj=null;
           try {
               obj=Class.forName(bd.getClassInfo()).newInstance();
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
           //这种原型模式创建的bean不能保存到beanMap中
           return obj;
       }
       if (this.beanMap.containsKey(beanid)){
           return this.beanMap.get(beanid);
       }
       if (bd.isIslazy()){
           Object obj=null;
           try {
               obj=Class.forName(bd.getClassInfo()).newInstance();
               //懒加载的bean是要保存的
               this.beanMap.put(beanid,obj);
               return obj;
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
           return  obj;
       }
       return null;
    }
}
