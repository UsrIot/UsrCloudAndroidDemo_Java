package cn.usr.usrcloudmqttsdkdemo.utils;

import java.math.BigInteger;

/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class BaseUtils {
    public static String bytes2hex01(byte[] bytes)
    {
        /**
         * 第一个参数的解释，记得一定要设置为1
         *  signum of the number (-1 for negative, 0 for zero, 1 for positive).
         */
        BigInteger bigInteger = new BigInteger(1, bytes);
        return bigInteger.toString(16);
    }
}
