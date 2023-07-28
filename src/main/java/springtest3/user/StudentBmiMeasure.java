package springtest3.user;

import org.springframework.stereotype.Component;
import springtest3.system.Measure;

//学生的Bmi指数测量接口
@Component("bmiMeasure")
public class StudentBmiMeasure implements Measure {
    //算法
    @Override
    public double doMeasure(Object obj) {
        if (obj==null){
            throw new RuntimeException("待数据异常");
        }
        if (!( obj instanceof  Student)){
            throw new RuntimeException("待数据异常");
        }
        Student s= (Student) obj;
        return s.getWeight()/(s.getHeight()*s.getHeight());
    }
}
