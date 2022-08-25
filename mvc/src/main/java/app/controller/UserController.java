package app.controller;

import app.annotation.OptLog;
import app.entity.R;
import app.entity.User;
import app.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public R update(@RequestBody User user) {
        return userService.update(user);
    }

    @PostMapping("/logout")
    public R quit(String token) {
        return userService.quit(token);
    }

    @GetMapping("/audit/need")
    public R findNeedAudit(@RequestHeader String token) {
        return userService.findNeedAudit(token);
    }

    @PutMapping("/audit/commit")
    @OptLog("提交审核操作")
    public R commitAudit(@RequestBody List<String> ids) {
        return userService.commitAudit(ids);
    }

    @GetMapping("/audit/finish")
    public R findFinishAudit() {
        return userService.findFinishAudit();
    }

    @PutMapping("/audit/rollback")
    @OptLog("回滚审核操作")
    public R rollbackAudit(@RequestBody List<String> ids) {
        return userService.rollbackAudit(ids);
    }
}
