package app.service.impl;

import app.cache.CacheClient;
import app.context.UserHolder;
import app.entity.R;
import app.entity.Topic;
import app.entity.Type;
import app.entity.User;
import app.mapper.ITopicMapper;
import app.service.ITopicService;
import app.service.ITypeService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.util.RedisConstant.*;

/**
 * 主贴业务实现
 *
 * @author 徐鑫
 */
@Service
@Slf4j
public class TopicServiceImpl implements ITopicService {

    @Resource
    private ITypeService typeService;
    @Resource
    private ITopicMapper topicMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    /**
     * 检查 token 的持有用户是否为 topic 的发布人
     *
     * @param topic 需要核查的topic
     * @return 如果持有人与发布人不同，则返回 -1，否则返回用户权限
     */
    private int checkUserId(Topic topic) {
        User user = UserHolder.getUser();
        if (topic.getUserId() == null || "".equals(topic.getUserId())) {
            topic = topicMapper.selectById(topic.getId());
        }
        if (user == null || !user.getId().equals(topic.getUserId())) {
            return -1;
        }
        return user.getPermission();
    }

    @Override
    public R release(Topic topic) {
        //检查格式是否正确
        if (topic.getUserId().length() != User.ID_LENGTH) {
            return R.fail("发帖人ID格式不正确");
        }
        //检查是否存在type
        Type type = typeService.getTypeById(topic.getTypeId());
        if (type == null) {
            return R.fail("帖子类型错误");
        }
        //检查发送topic的user是否与持有token的user一致
        if (checkUserId(topic) == -1) {
            return R.fail("token持有人与发布人不符！");
        }
        //将topic的数据补齐
        topic.setTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setId(IdUtil.getSnowflakeNextId());
        //向数据库中插入数据
        int count = topicMapper.insert(topic);
        if (count != 1) {
            return R.fail("服务器异常");
        }
        return R.success("发布成功").addData("time", topic.getTime()).addData("id", topic.getId());
    }

    @Override
    public R delete(Topic topic) {
        //删除topic只能由发帖人和管理员删除
        int count = 0;
        int permission = checkUserId(topic);
        if (permission == -1 || permission == User.TOURIST) {
            return R.fail("没有删除该主贴的权限！");
        }
        count = topicMapper.deleteById(topic.getId());
        if (count != 1) {
            return R.fail("主贴不存在！");
        }
        //删除缓存
        stringRedisTemplate.delete(TOPIC_ID_CACHE_KEY + topic);
        stringRedisTemplate.delete(TOPIC_TYPE_CACHE_KEY + topic.getTypeId());
        return R.success("删除成功！");
    }

    @Override
    public R update(Topic topic) {
        int permission = checkUserId(topic);
        if (permission == -1 || permission == User.TOURIST) {
            return R.fail("没有更新该topic的权限");
        }
        //更新修改时间
        topic.setTime(LocalDateTime.now());
        if (topicMapper.update(topic) != 1) {
            return R.fail("更新失败！");
        }
        //删除缓存
        stringRedisTemplate.delete(TOPIC_ID_CACHE_KEY + topic.getId());
        stringRedisTemplate.delete(TOPIC_TYPE_CACHE_KEY + topic.getTypeId());
        return R.success("更新成功！").addData("time", topic.getTime());
    }

    @Override
    public R findById(Long id) {

        if (id == null || id <= 0) {
            return R.fail("id格式错误");
        }

        Topic topic = cacheClient.queryWithPassThrough(TOPIC_ID_CACHE_KEY,
                id, Topic.class, topicMapper::getById, TOPIC_CACHE_TTL, TimeUnit.SECONDS);
        if (topic == null) {
            return R.fail("topic不存在");
        }
        return R.success().addData("topic", topic);
    }

    @Override
    public R findByType(Long type) {
        if (type == null || type <= 0) {
            return R.fail("类型格式错误");
        }
        List<Topic> topics = cacheClient.queryListWithPassThrough(TOPIC_TYPE_CACHE_KEY, type,
                Topic.class, topicMapper::getByType, TOPIC_CACHE_TTL, TimeUnit.SECONDS);

        if (topics == null || topics.isEmpty()) {
            return R.fail("该类型并无topic");
        }
        return R.success().addData("topics", topics);
    }

    @Override
    public R findByPage(Integer page, Integer pageSize) {

        List<Topic> topicList = null;
        try (Page<Object> objects = PageHelper.startPage(page, pageSize)) {
            topicList = topicMapper.getAllTopic();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }

        PageInfo<Topic> pageInfo = new PageInfo<>(topicList);

        return R.success().addData("info", pageInfo);
    }

    @Override
    public R findByUser(String userId) {
        List<Topic> topicList = topicMapper.getByUserId(userId);
        if (CollectionUtil.isEmpty(topicList)) {
            log.info("该用户并未发表任何讨论");
            return R.success("该用户并未发表任何讨论");
        }
        return R.success().addData("topics", topicList);
    }
}
