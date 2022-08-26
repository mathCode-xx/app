package app.util;

import app.entity.Secret;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理客户端唯一标识
 * @author 徐鑫
 */
public class SecretManager {

    private final Map<String, String> map = new HashMap<>();

    private SecretManager(){}

    private static volatile SecretManager instance;

    public static SecretManager getInstance() {
        if (instance == null) {
            synchronized (SecretManager.class) {
                if (instance == null) {
                    instance = new SecretManager();
                }
            }
        }
        return instance;
    }

    public void putClient(Secret secret) {
        map.put(secret.getClientKey(), secret.getClientSecret());
    }

    public boolean isContains(Secret secret) {
        return map.get(secret.getClientKey()) != null;
    }

}
