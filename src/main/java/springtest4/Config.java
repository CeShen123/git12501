package springtest4;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource(value = "classpath:db.properties")  //spring启动时 propertySource 类扫描 classpath:db.propertie
                                                   //以键值对存
public class Config {


}
