package cn.scut.app.service;

import cn.scut.app.entity.Comment;
import cn.scut.app.entity.R;

/**
 * 有关评论的业务接口
 * @author 徐鑫
 */
public interface ICommentService {

    /**
     * 发布评论
     * @param comment 需要发布的评论
     * @param token 用于检验是否为本人发送的
     * @return 操作结果
     */
    R release(Comment comment, String token);

    /**
     * 删除评论
     * @param comment 需要删除的评论
     * @param token 用于检测是否是自己的评论
     * @return 操作结果
     */
    R delete(Comment comment, String token);

    /**
     * 根据id查询评论内容
     * @param id 需要查询的评论id
     * @return 操作结果
     */
    R findById(Long id);

    R findByTopicId(Long topicId);
}
