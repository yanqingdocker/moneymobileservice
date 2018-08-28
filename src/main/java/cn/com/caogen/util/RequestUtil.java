package cn.com.caogen.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/8/16.
 */


public class RequestUtil {



    public static void main(String args[]){


        try {
            URL url = new URL("http://47.52.196.190/user/login1");
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();


            urlConnection.setRequestMethod("POST");
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            urlConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            urlConnection.setDoInput(true);
            // Post 请求不能使用缓存
            urlConnection.setUseCaches(false);


            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());

            String content = "telphone=" + "18193412366" + "&password=" + "12345";
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Object cook=urlConnection.getHeaderFields().get("Set-Cookie");
            String sessionid=null;
            StringBuffer ss=null;
            if(cook!=null){
                 sessionid=cook.toString();
                 ss=new StringBuffer(sessionid);
                ss.deleteCharAt(0);
            }


            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                //转化为UTF-8的编码格式
                line = new String(line.getBytes("UTF-8"));
                stringBuffer.append(line);
            }

            System.out.println(stringBuffer.toString());
            url = new URL("http://47.52.196.190/user/getuser");
          urlConnection =
                    (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("cookie", ss.toString());//sessionId

            urlConnection.connect();
            //返回打开连接读取的输入流，输入流转化为StringBuffer类型，这一套流程要记住，常用
             bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer sb=new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                //转化为UTF-8的编码格式
                line = new String(line.getBytes("UTF-8"));
                sb.append(line);
            }

            bufferedReader.close();
            System.out.println(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getStreamString(InputStream tInputStream){

        if (tInputStream != null){

    try{

         BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));

         StringBuffer tStringBuffer = new StringBuffer();

          String sTempOneLine = new String("");

         while ((sTempOneLine = tBufferedReader.readLine()) != null){
      tStringBuffer.append(sTempOneLine);
  }

        return tStringBuffer.toString();
    }catch (Exception ex){

         ex.printStackTrace();
    }

}

return null;

    }



    private static String readStream(InputStream is) {
        StringBuffer sb=new StringBuffer();
    try {
        int read = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((read = is.read()) != -1) {
            baos.write(read);
        }

        byte[] data = baos.toByteArray();
        baos.close();
       String s=new String(data);
       return s;
    }catch (Exception e){

    }

        return sb.toString();
    }



}
