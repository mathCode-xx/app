package app.symmetry;

import app.util.KeyConstant;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * 对称密钥生成器
 *
 * @author 徐鑫
 */
public class SymmetryGenerator {

    public static void create() {
        try {
            //密钥生成器
            KeyGenerator keyGen = KeyGenerator.getInstance(KeyConstant.SYMMETRY_MODEL);
            //默认128，获得无政策权限后可为192或256
            keyGen.init(128);
            //生成密钥
            SecretKey secretKey = keyGen.generateKey();
            SymmetryKey.key = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("对称密钥生成失败！");
        }
    }

}
