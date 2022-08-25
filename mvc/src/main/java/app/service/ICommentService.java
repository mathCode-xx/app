package app.service;

import app.entity.Comment;
import app.entity.R;

/**
 * 有关评论的业务接口
 *
 * @author 徐鑫
 */
public interface ICommentService {

    /**
     * 发布评论
     *
     * @param comment 需要发布的评论
     * @return 操作结果
     */
    R release(Comment comment);

    /**
     * 删除评论
     *
     * @param comment 需要删除的评论
     * @return 操作结果
     */
    R delete(Comment comment);

    /**
     * 根据id查询评论内容
     *
     * @param id 需要查询的评论id
     * @return 操作结果
     */
    R findById(Long id);

    R findByTopicId(Long topicId);
}
