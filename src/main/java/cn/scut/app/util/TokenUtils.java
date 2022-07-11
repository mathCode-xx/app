package cn.scut.app.util;

import cn.hutool.core.util.RandomUtil;

/**
 * 与token有关的工具类
 * @author 徐鑫
 */
public class TokenUtils {
    public static String getOnlyToken() {
        return RandomUtil.randomNumbers(6);
    }
}
