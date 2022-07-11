package cn.scut.app.service;

import cn.scut.app.entity.User;
import cn.scut.app.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    @Test
    void login() {
        //log.info(userService.login("201930035227").toString());
    }

    @Test
    void register() {
        User user = new User();
        user.setId("201930035229");
        user.setPassword("123456");
        user.setName("xuxin");
        user.setCollege("数学学院");
        user.setMajor("信息与计算科学");
        user.setSex('男');
        userService.register(user);
    }
}
