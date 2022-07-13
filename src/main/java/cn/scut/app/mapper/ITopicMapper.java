package cn.scut.app.mapper;

import cn.scut.app.entity.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 与数据库topic表的映射
 * @author 徐鑫
 */
@Mapper
public interface ITopicMapper {

    /**
     * 对topic进行插入操作
     * @param topic 需要插入的数据
     * @return 受影响的行数
     */
    @Insert("INSERT INTO topic(id, content, user_id, type_id, time)" +
            " VALUE(#{id}, #{content}, #{userId}, #{typeId}, #{time});")
    int insert(Topic topic);

    /**
     * 根据id和发布人id删除数据库中数据
     * @param id 需要删除的数据的id
     * @param userId 删除数据的用户id
     * @return 受影响的行数
     */
    @Delete("delete from topic where id = #{id} and user_id = #{userId}")
    int deleteByIdAndUser(long id, String userId);

    /**
     * 根据id删除数据库中数据
     * @param id 需要删除的数据的id
     * @return 受影响的行数
     */
    @Delete("delete from topic where id = #{id}")
    int deleteById(long id);

    /**
     * 根据id查询topic
     * @param id 需要查询的id
     * @return 查询到的数据
     */
    @Select("select * from topic where id = #{id};")
    Topic selectById(long id);

    /**
     * 修改topic表中的数据
     * @param topic 需要修改的topic
     * @return 受影响的行数
     */
    @Update("update topic set content = #{content}, type_id = #{typeId} where id = #{id};")
    int update(Topic topic);

    /**
     * 根据id从数据库中获取topic
     * @param id 需要查询的topic的id
     * @return 查询到的结果
     */
    @Select("select * from topic where id = #{id};")
    Topic getById(Long id);

    /**
     * 根据type从数据库中查找数据，封装成list集合
     * @param type 需要查询的type
     * @return topic集合
     */
    @Select("select * from topic where type_id = #{type};")
    List<Topic> getByType(Long type);
}
