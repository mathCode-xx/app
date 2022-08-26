package app.service.impl;

import app.asymmetry.AsymmetryKey;
import app.cache.CacheClient;
import app.context.UserHolder;
import app.entity.R;
import app.entity.Secret;
import app.entity.User;
import app.mapper.IUserMapper;
import app.service.IUserService;
import app.util.RedisConstant;
import app.util.SecretManager;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static app.util.RedisConstant.LOGIN_USER_KEY_PREFIX;

/**
 * UserService的实现类
 *
 * @author 徐鑫
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public R initLogin(Secret secret) {
        boolean contains = SecretManager.getInstance().isContains(secret);
        if (contains) {
            return R.success("客户端已经注册");
        }
        String clientSecret = secret.getClientSecret();
        log.info(clientSecret);
        byte[] decode = Base64.decode(clientSecret);
        byte[] decrypt = AsymmetryKey.decrypt(decode);
        String encode = Base64.encode(decrypt);
        secret.setClientSecret(encode);
        SecretManager.getInstance().putClient(secret);
        return R.success("客户端注册成功");
    }

    @Override
    public R login(User u) {
        // 没有登录，查询mysql
        User user = userMapper.getUserById(u.getId());
        //log.info(user.toString());
        if (user == null || !user.getPassword().equals(u.getPassword())) {
            return R.fail("学号或密码错误");
        }
        // 如果账户存在，创建唯一token，存入redis并返回
        String token = RandomUtil.randomNumbers(6);

        String jsonData;
        user.setPassword("");
        jsonData = JSONUtil.toJsonStr(user);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + token, jsonData, RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
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
        String token = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(RedisConstant.LOGIN_TOKEN_KEY_PREFIX + u.getId() + u.getPassword(),
                token, RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

        //将账户的其它信息补齐
        user = u;
        if (user.getSex() == 0) {
            user.setSex('女');
        }
        user.setScore(0);
        user.setStatus(User.NORMAL_STATUS);
        user.setPermission(User.TOURIST);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        //将数据存入mysql和redis中
        if (userMapper.insertUser(user) != 1) {
            return R.fail("服务器异常");
        }
        //保存到redis的数据应去除密码
        user.setPassword("");
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX + token,
                JSONUtil.toJsonStr(user), RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

        //将token和账户信息返回给前端
        return R.success().addData("token", token).addData("user", user);
    }

    @Override
    public R getUserById(String id) {
        if (id == null || id.length() != User.ID_LENGTH) {
            return R.fail("该用户不存在!");
        }
        return R.success().addData("user", userMapper.getUserById(id));
    }

    @Override
    public R update(User user) {
        User checkUser = UserHolder.getUser();
        int permission = checkUser.getPermission();
        if (permission == User.NORMAL_USER || permission == User.TOURIST) {
            //普通用户和游客不能修改自己的权限和账户状态以及他人的信息
            if (permission != user.getPermission()
                    || checkUser.getStatus() != user.getStatus()
                    || !checkUser.getId().equals(user.getId())) {
                return R.fail("权限不足");
            }
        } else if (permission == User.MANAGER) {
            //管理员的权限
            if (checkUser.getId().equals(user.getId())) {
                if (User.MANAGER != user.getPermission()
                        || checkUser.getStatus() != user.getStatus()) {
                    //如果管理员是修改自己的信息，那么就不能修改自己的权限和账户状态
                    return R.fail("管理员不能修改自己的权限和账户状态！");
                }
            } else {
                if (user.getPermission() != User.TOURIST && user.getPermission() != User.NORMAL_USER) {
                    //如果管理员不是修改自己的信息，那么只能管理普通用户和游客
                    return R.fail("权限不足");
                }
                if (user.getPassword() != null && !"".equals(user.getPassword())) {
                    return R.fail("管理员不能修改他人密码！");
                }
            }
        } else {
            //系统管理员有关权限
            if (checkUser.getId().equals(user.getId())) {
                if (User.SYSTEM_MANAGER != user.getPermission()
                        || checkUser.getStatus() != user.getStatus()) {
                    //如果系统管理员是修改自己的信息，那么就不能修改自己的权限和账户状态
                    return R.fail("管理员不能修改自己的权限和账户状态！");
                }
            } else {
                if (user.getPermission() == User.SYSTEM_MANAGER) {
                    //系统管理员不能将其他账户提升为系统管理员
                    return R.fail("不能将其他用户升级为系统管理员");
                }
                if (user.getPassword() != null && !"".equals(user.getPassword())) {
                    return R.fail("管理员不能修改他人密码！");
                }
            }
        }

        //修改最新update时间
        user.setUpdateTime(LocalDateTime.now());

        return userMapper.update(user) != 1
                ? R.fail("修改失败！")
                : R.success().addData("updateTime", user.getUpdateTime());
    }

    @Override
    public R quit(String token) {
        return cacheClient.delete(LOGIN_USER_KEY_PREFIX + token)
                ? R.success("退出成功！") : R.fail("退出失败，请重试！");
    }

    @Override
    public R findNeedAudit(String token) {
        User operator = cacheClient.getObjFromRedis(RedisConstant.LOGIN_TOKEN_KEY_PREFIX + token, User.class);
        if (operator.getPermission() == User.NORMAL_USER || operator.getPermission() == User.TOURIST) {
            return R.fail("权限不足");
        }
        List<User> users = userMapper.findNeedAudit();
        if (CollectionUtil.isEmpty(users)) {
            return R.success("以处理完所有用户");
        }
        return R.success().addData("users", users);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R commitAudit(List<String> ids) {

        if (!UserHolder.getUser().isManager()) {
            return R.fail("权限不足！");
        }
        int count = userMapper.commitAudit(ids);
        if (count != ids.size()) {
            throw new RuntimeException("操作失败");
        }
        return R.success("提交成功");
    }

    @Override
    public R findFinishAudit() {
        User user = UserHolder.getUser();
        if (!user.isManager()) {
            return R.fail("权限不足!");
        }
        List<User> finishAudit = userMapper.findFinishAudit();
        return R.success().addData("users", finishAudit);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public R rollbackAudit(List<String> ids) {
        if (!UserHolder.getUser().isManager()) {
            return R.fail("权限不足！");
        }
        int count = userMapper.rollbackAudit(ids);
        if (count != ids.size()) {
            throw new RuntimeException("操作失败！");
        }

        return R.success("操作成功！");
    }
}
