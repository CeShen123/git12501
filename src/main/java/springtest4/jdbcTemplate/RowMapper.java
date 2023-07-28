package springtest4.jdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
   /*
    对第i行的ResultSet 转换成   T对象 这个具体的实现由用户完成

    */
    public T mapper(ResultSet rs,int i) throws SQLException;
}
