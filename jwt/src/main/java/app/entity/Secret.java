package app.entity;

import lombok.Data;

/**
 * 客户端唯一标识
 * @author 徐鑫
 */
@Data
public class Secret {
    private String clientKey;
    private String clientSecret;
}
