package cn.scut.app.mapper;

import cn.scut.app.entity.Topic;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 与数据库topic表的映射
 *
 * @author 徐鑫
 */
@Mapper
public interface ITopicMapper {

    /**
     * 对topic进行插入操作
     *
     * @param topic 需要插入的数据
     * @return 受影响的行数
     */
    @Insert("INSERT INTO topic(id, content, user_id, type_id, time, update_time)" +
            " VALUE(#{id}, #{content}, #{userId}, #{typeId}, #{time}, #{updateTime});")
    int insert(Topic topic);

    /**
     * 根据id删除数据库中数据
     *
     * @param id 需要删除的数据的id
     * @return 受影响的行数
     */
    @Delete("delete from topic where id = #{id}")
    int deleteById(long id);

    /**
     * 根据id查询topic
     *
     * @param id 需要查询的id
     * @return 查询到的数据
     */
    @Select("select * from topic where id = #{id};")
    Topic selectById(long id);

    /**
     * 修改topic表中的数据
     *
     * @param topic 需要修改的topic
     * @return 受影响的行数
     */
    @Update("update topic set content = #{content}, type_id = #{typeId}, update_time = #{updateTime} " +
            "where id = #{id};")
    int update(Topic topic);

    /**
     * 根据id从数据库中获取topic
     *
     * @param id 需要查询的topic的id
     * @return 查询到的结果
     */
    @Select("select * from topic where id = #{id};")
    Topic getById(Long id);

    /**
     * 根据type从数据库中查找数据，封装成list集合
     *
     * @param type 需要查询的type
     * @return topic集合
     */
    @Select("select * from topic where type_id = #{type};")
    List<Topic> getByType(Long type);

    /**
     * 获取所有帖子
     *
     * @return 获取到的数据
     */
    @Select("select * from topic")
    List<Topic> getAllTopic();

    PageInfo<Topic> findAllUserByPageS(int pageNum, int pageSize);

    /**
     * 更新点赞数
     *
     * @param id        topic id
     * @param likeCount 需要增加的点赞数
     * @return 受影响的行数
     */
    @Update("update topic set like_count = like_count + #{likeCount}" +
            "where id = #{id};")
    Integer updateLikeCount(Long id, int likeCount);


    /**
     * 更新评论数
     *
     * @param id           topic id
     * @param commentCount 需要增加的评论数
     * @return 受影响的行数
     */
    @Update("update topic set comment_count = comment_count + #{commentCount}" +
            "where id = #{id};")
    Integer updateCommentCount(Long id, int commentCount);
}
