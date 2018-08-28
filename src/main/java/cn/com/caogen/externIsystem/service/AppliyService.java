package cn.com.caogen.externIsystem.service;

import cn.com.caogen.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * author:huyanqing
 * Date:2018/5/7
 */
public class AppliyService {
    private static final String TOKEN_RAW="grant_type=client_credentials&client_id=a5b044caa25e4a93a922411d216b83fa&client_secret=efcc0fd53b304bad998707b9c22636fd&scopes=alipay";
    private static final String TOKEN_URL="https://zx-api.juhe.cn/oauth2/token";
    private static final String TASK_GET_RAW="{'login_type':'qrcode','user_id':'userid'}";
    private static final String  TASK_GET_URL="https://zx-api.juhe.cn/alipay/v1/tasks";
    private static final String  IMAGE_BASE64_GET_URL="https://zx-api.juhe.cn/alipay/v1/tasks/%s/status";
    private static final String  APPLY_GET_RESULT_URL="https://zx-api.juhe.cn/alipay/v1/tasks/%s/result";
    private  static String token="Bearer ";
    private  static String taskId="";

    public static void init(){

        try {
            getToken();
            getTaskId();
        }catch (Exception e){
        }
    }
    public  static String scanLogin(String userid){
        try {

            String loginimgbase64=getImgebase64();
            if(StringUtil.checkStrs(loginimgbase64)){
                return loginimgbase64;
            }else {
                return "";
            }
        }catch (Exception e){
            return "";
        }


    }

    public static  String scanAllow(String userid){
        try {
            String allowimgbase64=getImgebase64();
            if(StringUtil.checkStrs(allowimgbase64)){
                return allowimgbase64;
            }else {
                return "";
            }

        }catch (Exception e){
            return "";
        }
    }

    /**
     * 获取token
     * @return
     * @throws Exception
     */
    public  static void getToken() throws Exception{
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(TOKEN_URL);
        StringEntity postingString = new StringEntity(TOKEN_RAW);// json传递
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        HttpResponse response = httpClient.execute(post);
        String content = EntityUtils.toString(response.getEntity());
        if(content!=null){
            if(JSONObject.fromObject(content).get("access_token")!=null){
                token=token+JSONObject.fromObject(content).get("access_token").toString();

            }
        }

    }

    /**
     * 获取taskId
     * @return
     * @throws Exception
     */
    public  static void getTaskId() throws Exception{
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(TASK_GET_URL);
        JSONObject jsonObject=JSONObject.fromObject(TASK_GET_RAW);
        StringEntity postingString = new StringEntity(jsonObject.toString());// json传递
        post.setEntity(postingString);
        post.setHeader("Authorization", token);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);
        String content = EntityUtils.toString(response.getEntity());
        if(content!=null){
            if(JSONObject.fromObject(content).get("task_id")!=null){
                taskId= JSONObject.fromObject(content).get("task_id").toString();

            }
        }

    }

    /**
     * 获取支付宝二维码图片Imgebase64
     * @return
     * @throws Exception
     */
    public  static String getImgebase64() throws Exception{
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(String.format(IMAGE_BASE64_GET_URL,taskId));
        get.setHeader("Authorization", token);
        get.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        if(content!=null){
            if(JSONObject.fromObject(content).get("input")!=null){
                JSONObject jsonObject=JSONObject.fromObject(content);
                String input=jsonObject.getJSONObject("input").getString("value");
                return input.split(",")[1];
            }
        }
        return "";


    }


    public  JSONObject getResult() throws Exception{
        JSONObject jsonObject=null;
        HttpClient httpClient = new DefaultHttpClient();
        String uri=String.format(APPLY_GET_RESULT_URL,taskId);
        HttpGet get = new HttpGet(String.format(APPLY_GET_RESULT_URL,taskId));
        get.setHeader("Authorization", token);
        get.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(get);
        String content = EntityUtils.toString(response.getEntity());
        if(content!=null){
            if(JSONObject.fromObject(content).get("user_info")!=null){
                 jsonObject=JSONObject.fromObject(content).getJSONObject("user_info");
                taskId="";
                token="Bearer ";
               return jsonObject;
            }
        }
        return null;
    }









}
