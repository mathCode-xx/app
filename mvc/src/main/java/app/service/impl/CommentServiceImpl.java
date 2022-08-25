package app.service.impl;

import app.cache.CacheClient;
import app.context.UserHolder;
import app.entity.Comment;
import app.entity.R;
import app.entity.Topic;
import app.entity.User;
import app.mapper.ICommentMapper;
import app.mapper.ITopicMapper;
import app.service.ICommentService;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.util.RedisConstant.*;


/**
 * CommentService的实现类
 *
 * @author 徐鑫
 */
@Service
public class CommentServiceImpl implements ICommentService {

    @Resource
    private ICommentMapper commentMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Resource
    private ITopicMapper topicMapper;

    /**
     * 检查操作的评论是否是自己发表的
     *
     * @param comment 评论
     * @return 操作人权限或-1（表示不是自己发表的评论）
     */
    private int checkUser(Comment comment) {
        User user = UserHolder.getUser();

        if (!user.getId().equals(comment.getUserId())) {
            return -1;
        }
        return user.getPermission();
    }

    @Override
    @Transactional
    public R release(Comment comment) {

        int permission = checkUser(comment);
        if (permission == -1 || permission == User.TOURIST) {
            return R.fail("权限不足！");
        }
        //检查发表评论所在的主贴是否存在
        if (cacheClient.queryWithPassThrough(TOPIC_ID_CACHE_KEY,
                comment.getTopicId(), Topic.class, topicMapper::getById,
                TOPIC_CACHE_TTL, TimeUnit.SECONDS) == null) {
            return R.fail("主贴不存在，发表失败！");
        }
        //补全评论信息
        comment.setTime(LocalDateTime.now());
        comment.setId(IdUtil.getSnowflakeNextId());

        int count = commentMapper.insert(comment);
        if (count != 1) {
            return R.fail("操作失败，请重试!");
        }
        topicMapper.updateCommentCount(comment.getTopicId(), 1);
        //删除redis缓存
        stringRedisTemplate.delete(COMMENT_TOPIC_CACHE_KEY + comment.getTopicId());
        return R.success().addData("comment", comment);
    }

    @Override
    public R delete(Comment comment) {
        int permission = checkUser(comment);
        if (permission == User.TOURIST) {
            return R.fail("权限不足！");
        } else if (permission == -1) {
            //如果permission==-1，表示token持有人和发表评论的不是同一个人，但是主贴发表人可以删除底下的任何评论
            Topic topic = cacheClient.queryWithPassThrough(TOPIC_ID_CACHE_KEY,
                    comment.getTopicId(), Topic.class, topicMapper::getById, TOPIC_CACHE_TTL, TimeUnit.SECONDS);
            User user = UserHolder.getUser();
            if (topic == null || topic.getUserId() == null || !topic.getUserId().equals(user.getId())) {
                return R.fail("权限不足！");
            }
            return R.success("删除成功！");
        }
        int count = commentMapper.delete(comment);
        if (count != 1) {
            return R.fail("评论已被删除！");
        }
        //删除缓存
        stringRedisTemplate.delete(COMMENT_ID_CACHE_KEY + comment.getId());
        stringRedisTemplate.delete(COMMENT_TOPIC_CACHE_KEY + comment.getTopicId());
        return R.success("删除成功！");
    }

    @Override
    public R findById(Long id) {
        if (id <= 0) {
            return R.fail("id格式错误");
        }
        Comment comment = cacheClient.queryWithPassThrough(COMMENT_ID_CACHE_KEY,
                id, Comment.class, commentMapper::selectById, COMMENT_CACHE_TTL, TimeUnit.SECONDS);
        return comment == null ? R.fail("评论不存在！") : R.success().addData("comment", comment);
    }

    @Override
    public R findByTopicId(Long topicId) {
        if (topicId <= 0) {
            return R.fail("topicId格式错误");
        }
        List<Comment> comments = cacheClient.queryListWithPassThrough(
                COMMENT_TOPIC_CACHE_KEY, topicId, Comment.class,
                commentMapper::selectByTopicId, COMMENT_CACHE_TTL, TimeUnit.SECONDS);

        if (comments == null || comments.isEmpty()) {
            return R.fail("不存在评论！");
        }
        return R.success().addData("comments", comments);
    }
}
