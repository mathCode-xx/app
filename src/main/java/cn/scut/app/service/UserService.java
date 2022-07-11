package cn.scut.app.service;

import cn.scut.app.entity.R;
import cn.scut.app.entity.User;

/**
 *
 * @author 徐鑫
 */
public interface UserService {
    /**
     * 登录业务
     * @param user 前端发送过来的账户信息
     * @return 登录状态
     */
    R login(User user);

    /**
     * 注册业务
     * @param user 前端发送过来的账户信息
     * @return 注册成功与否
     */
    R register(User user);
}
