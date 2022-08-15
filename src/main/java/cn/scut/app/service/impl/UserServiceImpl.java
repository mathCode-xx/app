package cn.scut.app.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import cn.scut.app.entity.R;
import cn.scut.app.entity.User;
import cn.scut.app.mapper.IUserMapper;
import cn.scut.app.service.IUserService;
import cn.scut.app.util.CacheClient;
import cn.scut.app.util.Constant;
import cn.scut.app.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static cn.scut.app.util.Constant.LOGIN_USER_KEY_PREFIX;

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

    @Resource
    private Utils utils;

    @Resource
    private CacheClient cacheClient;
    @Override
    public R login(User u) {
        // 没有登录，查询mysql
        User user = userMapper.getUserById(u.getId());
        //log.info(user.toString());
        if (user == null || !user.getPassword().equals(u.getPassword())) {
            return R.fail("学号或密码错误");
        }
        log.info(user.toString());
        // 如果账户存在，创建唯一token，存入redis并返回
        String token = RandomUtil.randomNumbers(6);

        String jsonData;
        user.setPassword("");
        jsonData = JSONUtil.toJsonStr(user);
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX+token, jsonData, Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
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
        stringRedisTemplate.opsForValue().set(Constant.LOGIN_TOKEN_KEY_PREFIX+u.getId()+u.getPassword(),
                token,Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

        //将账户的其它信息补齐
        user = u;
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
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY_PREFIX+token,
                JSONUtil.toJsonStr(user), Constant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);

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
    public R update(User user, String token) {
        User checkUser = utils.getBean(LOGIN_USER_KEY_PREFIX + token, User.class);
        int permission = checkUser.getPermission();
        if (permission == User.NORMAL_USER || permission == User.TOURIST) {
            //普通用户和游客不能修改自己的权限和账户状态以及他人的信息
            if (permission != user.getPermission()
                    ||checkUser.getStatus() != user.getStatus()
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
}
