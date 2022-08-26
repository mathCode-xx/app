package app.service;

import app.entity.R;
import app.entity.Secret;
import app.entity.User;

import java.util.List;

/**
 *
 * @author 徐鑫
 */
public interface IUserService {

    /**
     * 初始化登录，交换密钥
     * @param secret
     * @return
     */
    R initLogin(Secret secret);

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

    /**
     * 根据用户名查询用户信息
     * @param id 需要查询的用户id
     * @return 操作结果
     */
    R getUserById(String id);

    /**
     * 更新user信息
     * @param user 最新的user
     * @return 操作信息
     */
    R update(User user);

    /**
     * 退出登录
     * @param token 退出账号持有的令牌
     * @return 操作信息
     */
    R quit(String token);

    /**
     * 获取需要审核的账户
     * @return
     */
    R findNeedAudit(String token);

    R commitAudit(List<String> ids);

    R findFinishAudit();

    R rollbackAudit(List<String> ids);
}
