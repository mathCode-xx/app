package cn.scut.app.util;

/**
 * 存储常量
 * @author 徐鑫
 */
public class Constant {
    /**
     * 用于存放redis中token的组名
     */
    public static String LOGIN_TOKEN_KEY_PREFIX = "app:token:";
    /**
     * 用于存放redis中user的组名
     */
    public static String LOGIN_USER_KEY_PREFIX = "app:user:";
    /**
     * 用于存放redis中token存在的时长
     */
    public static int LOGIN_TOKEN_TTL = 3600;
}
