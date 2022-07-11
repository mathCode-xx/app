package cn.scut.app.mapper;

import cn.scut.app.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * User类和数据库的映射
 * @author 徐鑫
 */
@Mapper
public interface UserMapper {
    /***
     * 根据学号获取用户信息
     * @param id 学号
     * @return 用户实体
     */
    @Select("select * from student where id = #{id}")
    User getUserById(String id);

    /**
     * 向数据库插入账户
     * @param user 需要添加的账户
     * @return 返回所影响的行数
     */
    @Insert("insert into student(id, name, password, sex, college, major) " +
            "values(#{id}, #{name}, #{password}, #{sex}, " +
            "#{college}, #{major});")
    int insertUser(User user);
}
