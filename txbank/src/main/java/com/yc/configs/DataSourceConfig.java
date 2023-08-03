package com.yc.configs;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
@Data //lombok创建get/set方法
@Log4j2
public class DataSourceConfig {

    //利用Di将db.properties的内容注入
    @Value("${coreSize}")
    private int coreSize;
    @Value("${user}")
    private String user;
    @Value("${password}")
    private String password;
    @Value("${url}")
    private String url;
    @Value("com.mysql.cj.jdbc.Driver")
    private String driverclass;
    //以上属性从db.properties中读取出来后 都存到了 spring 容器中 Environment的变量(系统环境变量也在这里)
    @Value("#{T(Runtime).getRuntime().availableProcessors()*2}")
    //spEL ->spring expression  language
    private int cpuCount;

    //参数:第三方的框架中的类 由@Bean托管
    @Bean(initMethod = "init",destroyMethod = "close")//DruidDataSource中提供了  init初始化方法
    public DruidDataSource druidDataSource(){
        //另外要注意:idea对这个方法的返回值进行解析 判断是否有init
        DruidDataSource dds=new DruidDataSource();        dds.setUrl(url);
        dds.setUsername(user);
        dds.setPassword(password);
        dds.setDriverClassName(driverclass);
        //以上只是配置了参数 并没有创建连接池  在这个类的init()中完成了连接池的创建
        //当前主机的CPU数*2
        log.info("配置druid的连接池大小"+cpuCount);
        dds.setInitialSize(cpuCount);
        dds.setMaxActive(cpuCount*2);
        return dds;
    }

    @Bean//IOC注解 托管第三方的bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource =new DriverManagerDataSource();
        dataSource.setDriverClassName(driverclass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public DataSource dbcpDataSource(){
        BasicDataSource dataSource =new BasicDataSource();
        dataSource.setDriverClassName(driverclass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
