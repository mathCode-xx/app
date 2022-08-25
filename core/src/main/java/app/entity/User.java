package app.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * @author 徐鑫
 */
@Data
public class User {
    private String id;
    private String name;
    private String password;
    /**
     * 用户权限：0-系统管理员，1-普通管理员，2-普通用户，3-游客
     */
    private int permission;
    private char sex;
    private String college;
    private String major;
    private int score;
    private int status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isManager() {
        return permission == MANAGER || permission == SYSTEM_MANAGER;
    }

    public boolean isTourist() {
        return permission == TOURIST;
    }

    /**
     * 学号长度
     */
    public static final int ID_LENGTH = 12;
    /**
     * 游客
     */
    public static final int TOURIST = 3;
    /**
     * 普通用户
     */
    public static final int NORMAL_USER = 2;
    /**
     * 管理员
     */
    public static final int MANAGER = 1;
    /**
     * 系统管理员
     */
    public static final int SYSTEM_MANAGER = 0;
    /**
     * 正常状态
     */
    public static final int NORMAL_STATUS = 0;
    /**
     * 禁言状态
     */
    public static final int NO_SPEAK_STATUS = 1;
    /**
     * 封禁状态
     */
    public static final int BANNED_STATUS = 2;
}
