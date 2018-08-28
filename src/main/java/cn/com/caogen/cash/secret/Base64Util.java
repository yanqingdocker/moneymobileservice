package cn.com.caogen.cash.secret;

import cn.com.caogen.cash.secret.Base64Local;

/**
 * 处理http请求转码问题Base64加密工具类
 * @author hardyHuang
 */
public class Base64Util {
    /**
     * base64加密
     * @param str
     * @return
     * @throws Exception
     */
    public static String encode(String str) throws Exception{
        str = Base64Local.encodeToString(str.getBytes("UTF-8"), false);
        str = str.replace("+", "*");
        str = str.replace("/", "-");
        str = str.replace("=", ".");
        return str;
    }

    /**
     * base64解密
     * @param str
     * @return
     * @throws Exception
     */
    public static String Base64DecodeReplaceSpecialChar(String str) throws Exception {
        str = str.replace("*", "+");
        str = str.replace("-", "/");
        str = str.replace(".", "=");
        str = new String(Base64Local.decode(str), "UTF-8");
        return str;

    }

}
