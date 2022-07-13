package cn.scut.app.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import cn.scut.app.entity.Comment;
import cn.scut.app.entity.R;
import cn.scut.app.entity.User;
import cn.scut.app.mapper.ICommentMapper;
import cn.scut.app.mapper.ITopicMapper;
import cn.scut.app.service.ICommentService;
import cn.scut.app.util.CacheClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.scut.app.util.Constant.*;

/**
 * CommentService的实现类
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

    private int checkUser(Comment comment, String token) {
        String userJson = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY_PREFIX + token);
        User user = JSONUtil.toBean(userJson, User.class);

        if (!user.getId().equals(comment.getUserId())) {
            return -1;
        }
        return user.getPermission();
    }

    @Override
    public R release(Comment comment, String token) {

        int permission = checkUser(comment, token);
        if (permission == -1 || permission == User.TOURIST) {
            return R.fail("权限不足！");
        }
        if (topicMapper.selectById(comment.getTopicId()) == null) {
            return R.fail("主贴不存在，请重试！");
        }
        comment.setTime(LocalDateTime.now());
        comment.setId(IdUtil.getSnowflakeNextId());

        int count = commentMapper.insert(comment);
        return count != 1 ? R.fail("操作失败！请重试")
                : R.success().addData("time", comment.getTime()).addData("id", comment.getId());
    }

    @Override
    public R delete(Comment comment, String token) {
        int permission = checkUser(comment, token);
        if (permission == -1 || permission == User.TOURIST) {
            return R.fail("权限不足！");
        }
        int count = commentMapper.delete(comment);
        return count != 1 ? R.fail("评论已被删除！")
                : R.success("删除成功！");
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
