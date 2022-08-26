package app.symmetry;

import org.junit.Test;
import org.springframework.util.Base64Utils;

public class SymmetryKeyTest {


    @Test
    public void encrypt() {
        SymmetryGenerator.create();

        String s ="你好";
        byte[] encrypt = SymmetryKey.encrypt(s.getBytes());
        System.out.println(new String(Base64Utils.encode(encrypt)));

        byte[] decrypt = SymmetryKey.decrypt(encrypt);

        System.out.println(new String(decrypt));
    }
}
