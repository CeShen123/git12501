package springtest3.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   //带所有参数构造方法
@NoArgsConstructor   //无参构造方法
public class Student {
    private String name;
    private double height;
    private double weight;

}
