package springtest1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration  //此类是一个配置类(这个类也会被spring创建)，声明容器运行时的一些配置信息(1.扫描的路径  这个路径所有带有@Component)
//spring托管
@ComponentScan(basePackages = {"springtest1"})
public class Config {
}
