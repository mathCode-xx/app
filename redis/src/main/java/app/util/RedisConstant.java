package app.util;

public class RedisConstant {
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

    public static int CACHE_NULL_TTL = 100;

    public static String LOCK_TOPIC_KEY = "lock:topic:";

    public static String TOPIC_ID_CACHE_KEY = "app:topic:id:";

    public static Long TOPIC_CACHE_TTL = 600L;

    public static String TOPIC_TYPE_CACHE_KEY = "app:topic:type:";

    public static String COMMENT_ID_CACHE_KEY = "app:comment:id:";

    public static Long COMMENT_CACHE_TTL = 600L;

    public static String COMMENT_TOPIC_CACHE_KEY = "app:comment:topic:";
}
