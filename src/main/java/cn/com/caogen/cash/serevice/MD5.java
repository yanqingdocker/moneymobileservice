package cn.com.caogen.cash.serevice;

import java.security.MessageDigest;

public class MD5 {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5() {
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for(int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            return resultString;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String MD5EncodeGB2312(String origin) {
        String resultString = origin;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("GB2312")));
            return resultString;
        } catch (Exception var4) {
            return null;
        }
    }

    public static String MD5EncodeGBK(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("GBK")));
            return resultString;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String MD5EncodeUTF8(String origin) {
        String resultString = null;

        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
            return resultString;
        } catch (Exception var3) {
            return null;
        }
    }

    public static void main(String[] args) {
        String ss = "433407550016000106634121800100001240001520136522604223906610663420130325185256046123||11111111";
        ss = "433407550016000004223906610663420130325185256046123||11111111";
        System.out.println(MD5EncodeGBK(ss).length());
        System.out.println(MD5EncodeUTF8(ss));
        System.out.println(MD5Encode(ss));

        try {
            System.out.println(MD5EncodeGB2312(ss));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
