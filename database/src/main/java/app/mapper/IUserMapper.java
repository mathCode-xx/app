package app.mapper;

import app.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * User类和数据库的映射
 * @author 徐鑫
 */
@Mapper
public interface IUserMapper {
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
    @Insert("insert into student(id, name, password, sex, college, major, create_time, update_time) " +
            "values(#{id}, #{name}, #{password}, #{sex}, " +
            "#{college}, #{major}, #{createTime}, #{updateTime});")
    int insertUser(User user);

    /**
     * 根据学号查询账户
     * @param id 需要查询的学号
     * @return 返回的账户
     */
    @Select("select * from student where id = #{id}")
    User getById(String id);

    /**
     * 更新用户信息
     * @param user 最新状态
     * @return 受影响的行数
     */
    @Update("<script>update student set " +
            "<if test= \"password != null and password != ''\"> password = #{password}, </if> " +
            "permission = #{permission}, score = #{score}, status = #{status}, update_time = #{updateTime}" +
            "where id = #{id};</script>")
    int update(User user);

    @Select("select * from student where permission = 3")
    List<User> findNeedAudit();

    @Update("<script>"
            + "update student set  permission = 2 "
            + "<where>" + "<foreach collection = \"ids\" item = \"id\" open = \"id in (\" close = \")\" separator = \",\">"
            + "#{id}"
            + "</foreach>" + "</where>" + "</script>")
    int commitAudit(List<String> ids);

    @Select("select * from student where permission = 2;")
    List<User> findFinishAudit();

    @Update("<script>"
            + "update student set  permission = 3 "
            + "<where>" + "<foreach collection = \"ids\" item = \"id\" open = \"id in (\" close = \")\" separator = \",\">"
            + "#{id}"
            + "</foreach>" + "</where>" + "</script>")
    int rollbackAudit(List<String> ids);
}
