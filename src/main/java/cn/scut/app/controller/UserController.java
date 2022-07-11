package cn.scut.app.controller;

import cn.scut.app.entity.R;
import cn.scut.app.entity.User;
import cn.scut.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 * @author 徐鑫
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user){
        if (user.getId().length() != User.ID_LENGTH) {
            return R.fail("学号长度不符合规则");
        }
        return userService.login(user);
    }

    @PostMapping("/register")
    public R register(@RequestBody User user) {
        if (user.getId().length() != User.ID_LENGTH) {
            return R.fail("学号长度不符合规则");
        }
        return userService.register(user);
    }
}
