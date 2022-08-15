package cn.scut.app.util;

import cn.hutool.json.JSONUtil;
import cn.scut.app.entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis中的value和bean之间的转换
 * @author 徐鑫
 */
@Component
public class Utils {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从redis中获取bean
     * @param key redis中保存bean的key
     * @param type bean的类型
     * @return 获取到的bean
     * @param <R> bean的类型
     */
    public <R> R getBean(String key, Class<R> type) {
        String jsonData = stringRedisTemplate.opsForValue().get(key);
        return JSONUtil.toBean(jsonData, type);
    }

    /**
     * 向redis中保存bean
     * @param key redis中保存bean的key
     * @param obj bean
     */
    public void saveBean(String key, Object obj) {
        String json = JSONUtil.toJsonStr(obj);
        stringRedisTemplate.opsForValue().set(key, json);
    }

    /**
     * 检查token的持有人id是否为传入的userId,或者持有人是管理员
     * @param userId 核对id
     * @param token 被检测的token
     * @return token持有人信息或者null（表示核对错误并且不是管理员）
     */
    public User checkUser(String userId, String token) {
        User user = getBean(Constant.LOGIN_USER_KEY_PREFIX + token, User.class);
        int permission = user.getPermission();
        if (permission == User.SYSTEM_MANAGER || permission == User.MANAGER || user.getId().equals(userId)) {
            //如果token的持有人是管理员或系统管理员，或者token持有人的id与传入的userId相同，返回token持有人的权限
            return user;
        }
        return null;
    }
}
