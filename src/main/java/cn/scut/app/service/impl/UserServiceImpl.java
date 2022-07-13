package cn.scut.app.service.impl;

import cn.hutool.json.JSONUtil;
import cn.scut.app.entity.R;
import cn.scut.app.entity.User;
import cn.scut.app.mapper.IUserMapper;
import cn.scut.app.service.IUserService;
import cn.scut.app.util.Constant;
import cn.scut.app.util.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * UserService的实现类
 * @author 徐鑫
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public R login(User u) {
        // 没有登录，查询mysql
        User user = userMapper.getUserById(u.getId());
        if (user == null || !user.getPassword().equals(u.getPassword())) {
            return R.fail("学号或密码错误");
        }
        // 如果账户存在，创建唯一token，存入redis并返回
        String token = TokenUtils.getOnlyToken();

        String jsonData;
        user.setPassword("");
        jsonData = JSONUtil.toJsonStr(user);
        stringRedisTemplate.opsForValue().set(Constant.LOGIN_USER_KEY_PREFIX+token, jsonData, Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
        return R.success().addData("token", token).addData("user", user);
    }

    @Override
    public R register(User u) {
        User user = userMapper.getUserById(u.getId());
        if (user != null) {
            //如果账户已经存在，则直接返回
            return R.fail("账户已存在");
        }

        //如果账户不存在，先生成token确保用户登录
        String token = TokenUtils.getOnlyToken();
        stringRedisTemplate.opsForValue().set(Constant.LOGIN_TOKEN_KEY_PREFIX+u.getId()+u.getPassword(),
                token,Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

        //将账户的其它信息补齐
        user = u;
        user.setScore(0);
        user.setStatus(User.NORMAL_STATUS);
        user.setPermission(User.NORMAL_USER);

        //将数据存入mysql和redis中
        if (userMapper.insertUser(user) != 1) {
            return R.fail("服务器异常");
        }
        //保存到redis的数据应去除密码
        user.setPassword("");
        stringRedisTemplate.opsForValue().set(Constant.LOGIN_USER_KEY_PREFIX+token,
                JSONUtil.toJsonStr(user), Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

        //将token和账户信息返回给前端
        return R.success().addData("token", token).addData("user", user);
    }

    @Override
    public User getUserById(String id) {
        if (id.length() != User.ID_LENGTH) {
            return null;
        }
        return userMapper.getUserById(id);
    }
}
