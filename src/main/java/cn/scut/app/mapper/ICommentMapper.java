package cn.scut.app.mapper;

import cn.scut.app.entity.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 与数据库中comment表的映射类
 * @author 徐鑫
 */
@Mapper
public interface ICommentMapper {

    /**
     * 向数据库中插入comment数据
     * @param comment 需要保存的数据
     * @return 受影响的行数
     */
    @Insert("insert into comment value (#{id},#{content},0,#{time},#{topicId},#{userId});")
    int insert(Comment comment);

    /**
     * 从数据库中删除评论
     * @param comment 需要删除的评论
     * @return 受影响的行数
     */
    @Delete("delete from comment where id = #{id};")
    int delete(Comment comment);

    @Select("select * from comment where id = #{id};")
    Comment selectById(Long id);

    @Select("select * from comment where topic_id = #{topicId}")
    List<Comment> selectByTopicId(Long topicId);
}
