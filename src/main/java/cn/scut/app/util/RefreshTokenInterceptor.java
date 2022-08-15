package cn.scut.app.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.scut.app.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 拦截所有请求，用来判断用户是否还在使用
 * @author 徐鑫
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //1、获取登录令牌
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            return true;
        }
        //2、获取redis中的用户
        String key = Constant.LOGIN_USER_KEY_PREFIX + token;
        String userJson = stringRedisTemplate.opsForValue().get(key);
        //3、判断用户是否存在
        if (userJson == null || "".equals(userJson)) {
            //4、如果不存在，放行
            return true;
        }
        //5、将查询到的json数据转为User对象
        User user = JSONUtil.toBean(userJson, User.class);
        //6、保存用户信息到ThreadLocal中
        UserHolder.saveUser(user);
        //7、如果存在，刷新key的时间
        stringRedisTemplate.expire(key, Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
