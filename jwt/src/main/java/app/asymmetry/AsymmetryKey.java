package app.asymmetry;

import cn.hutool.core.codec.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对称加密
 * @author 徐鑫
 */
public class AsymmetryKey {
    private static final String KEY_ALGORITHM = "RSA";
    protected static byte[] priKey = Base64.decode("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9okWJy7L4zq6sG9dWjcWePL99cz7F3ofG+uTWXBr0rDASk0I0xhnxTfAHrF6o9FoejkOb+8CDqp7o0waEjE+6xofh4XjOvoJdZePbg1FO3zeyjl4JAjmN2sbuaEjtrDFcYxdp/Ml2wRRpqOnV5SeQTLNeFOYyzMkKSjjLykwxNAgMBAAECgYAKQ0szkWlDMu8XIGhCDDcdjw8VTFO60z7ujMmTyli5Y8TET8V1hv4SPGBm0eHrfeoDauPGBh0joWKRucUgQLKPDi3h+/BqhS0vINrdsmXfjBM2MCsXsCGK9nkSloeqlcKmVMoJ5B7Sq0xEpvRNmVGg6T5oE+0OmJrnRAOFukw0aQJBAMNXDSiS28ymhO0LyOTJC5TiM2vJ16bxBveW5f1CCLsk9DfHpgi/wrgSccE+div4BwPYZ3Td7GgP31hOhAF99lsCQQDQ6RBVRWPDSHFva4c3+Ycy+S1NkWgRjSfphfWM+33LdJZSwLjXezamO3rMR1HXIpCjxxH1wTOg1ckaSXJ7Vxh3AkEAvApgsF4vGKJz830UtqiSbZAS0ABNiHcdrdmlLFcU8JJyRf/65REz8N5QgyYNu+XXWuxHK0k2+jXNWsS6bnVDYQJBAMvTeVIZH5r65WI2YXmlhdmf8CmMQyNMu7JGNyvUfAmzuK8Ixc/HsaxCwC7rLtheKw0AP2+tjvfPXiS9oGrisUUCQBwaXza9vfkl1Ak+xakdVGEsmh9YZFwQAAtvaygVX/BEGLbJyxEDrucrI4fFrZKd1I5ibh/KiL5aMI+zBWl/3Wg=");;
    protected static byte[] pubKey = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfaJFicuy+M6urBvXVo3Fnjy/fXM+xd6Hxvrk1lwa9KwwEpNCNMYZ8U3wB6xeqPRaHo5Dm/vAg6qe6NMGhIxPusaH4eF4zr6CXWXj24NRTt83so5eCQI5jdrG7mhI7awxXGMXafzJdsEUaajp1eUnkEyzXhTmMszJCko4y8pMMTQIDAQAB");


    public static byte[] encrypt(byte[] data) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("非对称加密出错！");
        }
    }

    public static byte[] decrypt(byte[] data) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(priKey);
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("非对称解密出错！");
        }
    }

    public static byte[] getPubKey() {
        return pubKey;
    }
}
