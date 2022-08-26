package app.asymmetry;

import app.symmetry.SymmetryGenerator;
import app.symmetry.SymmetryKey;
import cn.hutool.core.codec.Base64;
import org.junit.Test;

public class AsymmetryKeyTest {

    @Test
    public void encrypt() {
//        AsymmetryGenerator.create();
//        SymmetryGenerator.create();

        //String pubStr2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbJJj1bz4F/+kYgoRpGsz8rntDAh0ic1HPcbh5Bsh540cCxJhLtTp1YQBdhNZ39Mtl8hoguJcCrz5EiitrkAyJ/mN5gSwpVjRTSgn5+lXi7FnHNEdu8CKE3QYQ1qqCNJx11aYtGUR1RDLC52pb7YtEQdyqGdWdGpDVL+awJPjTtQIDAQAB";
//        String priStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJskmPVvPgX/6RiChGkazPyue0MCHSJzUc9xuHkGyHnjRwLEmEu1OnVhAF2E1nf0y2XyGiC4lwKvPkSKK2uQDIn+Y3mBLClWNFNKCfn6VeLsWcc0R27wIoTdBhDWqoI0nHXVpi0ZRHVEMsLnalvti0RB3KoZ1Z0akNUv5rAk+NO1AgMBAAECgYAG0dEEP0n53la5hEmcJSVyZgKp3pa3qo7W3SgMVq2XtG0Td6wKIYLautM9SSP0TSDNVmfeA4v/ywKkGKUkxHkIj9yL2TnVkx2/PxJ9c+fVGKiBMb5CaALAkhP1uCwSIzxqyM8R1aSoqTRpmTQcY+ClY77xinzEWKfpAgGMkyJbAQJBALmH/tKx4tDSkxSEetryHkCLC+CGy5+T/ETKy6tiKQo43Ck3rEXm54ailmYaXboOBdUuiUE4lyxceef+f3gKnoUCQQDWEdGcab8mUmX+Dt6ZRK7x1RuG/KAyvNVe4n69yPXpnG+e5tbam8Ri5btluCRzBh/SBuDOz5oHj7QLv++1V99xAkBm8y3VoFvp4E85HWJGx/89dWq7xlCLVkSvr5Bgo3F+eJrCTGBN0Zk7vlCSi6LisRFPgCkthcdtGEuVDkWRSOM5AkBhV37yWh+ipGteiJJuLY5Dx30Z97w89iMsX1/0mvI/xXP4VM20YIp/0ilRytdAOrOzhI8rW7mV4KGliiTNWGTxAkEApZwiALv8tte/5IzeexhCgJSBliK2jOY7kpUCnAiS/K55UAy+cbjrj8mzQmlcFyHuRA4UT1OQxE2SCRm4o6vLyA==";
//        String pubStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbJJj1bz4F/+kYgoRpGsz8rntDAh0ic1HPcbh5Bsh540cCxJhLtTp1YQBdhNZ39Mtl8hoguJcCrz5EiitrkAyJ/mN5gSwpVjRTSgn5+lXi7FnHNEdu8CKE3QYQ1qqCNJx11aYtGUR1RDLC52pb7YtEQdyqGdWdGpDVL+awJPjTtQIDAQAB";
//        //System.out.println("pub      " + pubStr.equals(pubStr2));
//        AsymmetryKey.priKey = Base64.decode(priStr);
//        AsymmetryKey.pubKey = Base64.decode(pubStr);

        String after = "hxskaNCBHvWh+7IvkK7Y7IR9CDHP39cQ8JeKHagslSHjYrvvQTRyzCsvag5HZ+AKcRcSh/w26CyvwcUygWtacZ3eP6hfABux2JIxkrNJXRZ/rXLp9GiYj5VioS8PoqtkkPTHXy2q+3Qv7cz8EMbNUaM0mTKFAwdiTpeH/DxyqkU=";
        String before = "PMYNDA18erhFGXlefEhh0w==";
//        String after2 = "dMiRK/Bm+lpnAzQxtQVPVSouVe/0Zg8MWZZngsKT4dPqyTk/wwEvzGjPD0ZvkACgOfP/9o3qx4hGXJPPMNgVSgsAjK+kPKMUGV09EqYPudXiTjb8zJ5ii+lD/r3LAyCHM8GrvR70+fYLQ1z2+uuinV2g/z4+UUR5oOY91OI/Kro=";

//        byte[] encrypt1 = AsymmetryKey.encrypt(Base64.decode(before));
//        System.out.println("1、"+Base64.encode(encrypt1));
//        //System.out.println(after.equals());
//
//        byte[] key = Base64.decode(before);
//        String secret = Base64.encode(key);
//        System.out.println("2、"+secret);
//
//        byte[] encrypt = AsymmetryKey.encrypt(Base64.decode(before));
//        String encode = Base64.encode(encrypt);
//
//        System.out.println("3、"+encode);

        byte[] decode = Base64.decode(after);

        byte[] decrypt = AsymmetryKey.decrypt(decode);

        System.out.println("4、"+Base64.encode(decrypt));
    }

}
