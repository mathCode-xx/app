package cn.scut.app.entity;

import lombok.Data;

/**
 * 用户实体类
 * @author 徐鑫
 */
@Data
public class User {
    private String id;
    private String name;
    private String password;
    private int permission;
    private char sex;
    private String college;
    private String major;
    private int score;
    private int status;
    /**
     * 学号长度
     */
    public static int ID_LENGTH = 12;
    /**
     * 游客
     */
    public static int TOURIST = 3;
    /**
     * 普通用户
     */
    public static int NORMAL_USER = 2;
    /**
     * 管理员
     */
    public static int MANAGER = 1;
    /**
     * 系统管理员
     */
    public static int SYSTEM_MANAGER = 0;
    /**
     * 正常状态
     */
    public static int NORMAL_STATUS = 0;
    /**
     * 禁言状态
     */
    public static int NO_SPEAK_STATUS = 1;
    /**
     * 封禁状态
     */
    public static int BANNED_STATUS = 2;
}
