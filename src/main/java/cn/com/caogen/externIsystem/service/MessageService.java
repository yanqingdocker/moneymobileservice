package cn.com.caogen.externIsystem.service;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;


public class MessageService {
	private static Logger logger = LoggerFactory.getLogger(MessageService.class);
	private static String chinaUrl = "http://106.wx96.com/webservice/sms.php?method=Submit";
	private static String No_chinaUrl = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";
	private static String chinaAccount = "C01409210";
	private static String chinaPassword = "b3add47d33a7368bfc546f51961a5173";
	private static String No_chinaAccount = "I03715129";
	private static String No_chinaPassword = "215695e0e88067dfc83a8a14c2682ce5";
	private static String pattern="^1[356789][0-9]{9}";

	public  static int checkPhone(String phone) {
		HttpClient client = new HttpClient();
		PostMethod method = null;
		NameValuePair account= new NameValuePair("account","");
		NameValuePair password= new NameValuePair("password","");
		if(Pattern.matches(pattern,phone)){
			account.setValue(chinaAccount);
			password.setValue(chinaPassword);
			method = new PostMethod(chinaUrl);

		}else{
			account.setValue(No_chinaAccount);
			password.setValue(No_chinaPassword);
			method = new PostMethod(No_chinaUrl);
		}

		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");
		int mobile_code = (int)((Math.random()*9+1)*100000);

	    String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");


		NameValuePair[] data = {
				account,
				password,
			    new NameValuePair("mobile", phone),
			    new NameValuePair("content", content),
		};
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult =method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			String code = root.elementText("code");

			logger.info("==================="+code);
			 if("2".equals(code)){
				return mobile_code;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}