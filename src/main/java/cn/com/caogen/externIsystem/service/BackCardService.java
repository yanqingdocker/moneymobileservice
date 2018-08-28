package cn.com.caogen.externIsystem.service;

import cn.com.caogen.util.ConstantUtil;
import cn.com.caogen.util.StringUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
public class BackCardService {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static final String CHECK_APPKEY_URL="http://v.juhe.cn/verifybankcard4/query.php";
    public static final String BANCTYPE_APPKEY_URL="http://v.juhe.cn/bankcardinfo/query";
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    //配置您申请的KEY
    public static final String CHECK_APPKEY ="826f4c622923a1da822485e83b588e24";

    public static final String BACKYPE_APPKEY ="5cbeb31f4ad11e8effff7c78474ec286";

    /**
     * 校验绑定的银行卡
     * @param username
     * @param idcard
     * @param backcardid
     * @param phone
     * @return
     */
    public static boolean checkBackCard(String username,String idcard,String backcardid,String phone){
        try{

            Map<String,String> params=new HashMap<String,String>();
            params.put("key",CHECK_APPKEY);//APP Key
            params.put("realname",username);
            params.put("idcard",idcard);
            params.put("bankcard",backcardid);
            params.put("mobile",phone);
            params.put("uorderid",String.valueOf(System.currentTimeMillis()));
            String result =net(CHECK_APPKEY_URL, params, "GET");
            if(!StringUtils.isEmpty(result)){
                JSONObject object = JSONObject.fromObject(result);
                String res=object.getJSONObject("result").getString("res");
                if(!StringUtils.isEmpty(res)&&ConstantUtil.BACKCARD_SUCCESS_CODE.equals(res)){
                    return  true;
                }
            }
        }catch (Exception e){

        }
        return false;
    }

    public static String getBackType(String bankcard){
        try{

            Map<String,String> params=new HashMap<String,String>();
            params.put("key",BACKYPE_APPKEY);//APP Key
            params.put("bankcard",bankcard);
            String result =net(BANCTYPE_APPKEY_URL, params, "GET");
            if(!StringUtils.isEmpty(result)){
                JSONObject object = JSONObject.fromObject(result);
                String logUrl=object.getJSONObject("result").getString("logo");
                String bankType=object.getJSONObject("result").getString("bank");
                String rs="{'logUrl':'"+logUrl+"','bankType':'"+bankType+"'}";

                return JSONObject.fromObject(rs).toString();
            }
        }catch (Exception e){

        }
        return "";
    }




    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,String>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
