package cn.scut.app.controller;

import cn.scut.app.entity.R;
import cn.scut.app.entity.User;
import cn.scut.app.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制层
 * @author 徐鑫
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

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

    @GetMapping("/{id}")
    public R findUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public R update(@RequestBody User user, @RequestHeader String token) {
        return userService.update(user, token);
    }

    @PostMapping("/logout")
    public R quit(String token) {
        return userService.quit(token);
    }
}
