

package cn.com.caogen.util;

import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;

public class StringUtil {
	public static String str;
	public static final String EMPTY_STRING = "";

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 转换字节数组为16进制字串
	 * @param b 字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}

	public static boolean checkStrs(String... args){
		for(String str:args){
			if(StringUtils.isEmpty(str)){
				return false;
			}
		}
		return true;
	}
	public static Object toBean(Class objClass, JSONObject jsonObject) throws Exception {
		Object obj = objClass.newInstance();
		Field[] fields = objClass.getDeclaredFields();
		for (Field field : fields) {
			StringBuffer fieldName = new StringBuffer(field.getName());
			if (jsonObject.has(fieldName.toString())) {
				String temp = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = objClass.getDeclaredMethod("set" + temp,field.getType());
				String type=field.getType().getName();
				if(type.equals("java.lang.String")){
					method.invoke(obj, jsonObject.getString(fieldName.toString()));
				}else if(type.equals("java.lang.Double")){
					method.invoke(obj, jsonObject.getDouble(fieldName.toString()));
				}else{
					method.invoke(obj, jsonObject.getInt(fieldName.toString()));
				}
			}
		}
		return obj;
	}
}
