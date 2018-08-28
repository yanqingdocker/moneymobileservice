package cn.com.caogen.cash.serevice;


import cn.com.caogen.cash.mode.JsonResult;
import cn.com.caogen.cash.secret.Base64Local;
import cn.com.caogen.cash.secret.SecurityRSAPay;
import cn.com.caogen.cash.util.JsonUtils;
import cn.com.caogen.cash.util.MerchantConstants;
import cn.com.caogen.cash.util.SimpleHttpsClient;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户订单demo业务层
 * @author hardyHuang
 */

public class PayforService {
    @Test
    public  void test() throws Exception{
        System.out.println(queryAccountBalance("CNY"));
    }
    /**
     * 查询账号余额
     * @return
     * @throws Exception
     * @date 2016-11-10 下午5:04:45
     */
    public static Double queryAccountBalance(String currencyName) throws Exception {
        String merId = MerchantConstants.MERID;
        String version = MerchantConstants.VERSION;
        String url = MerchantConstants.QUERYBALANCE;
        String serverPublicKey = MerchantConstants.SERVERPUBLICKEY;
        String privateKey = MerchantConstants.PRIVATEKEY;
        String terId = MerchantConstants.TERID;
        String apiKey = MerchantConstants.APIKEY;

        StringBuilder content = new StringBuilder();
        content.append("apiKey=").append(apiKey);
        if(StringUtils.isNotBlank(currencyName)){
            content.append("&currencyName=").append(currencyName) ;
        }
        String hmac = MD5.MD5EncodeUTF8(content.toString());

        Map<String, String> requestmap = new HashMap<String, String>();
        requestmap.put("apiKey", apiKey);
        requestmap.put("hmac", hmac);
        requestmap.put("currencyName", currencyName);
        // 验证参数是否有null值
        Map<String, String> requestParam = getRequestMap(JsonUtils.objectToJson(requestmap), serverPublicKey, privateKey, version, merId, terId, "utf-8");

        // 请求
        String info = new SimpleHttpsClient().postRequest(url,requestParam, 60000).getResponseBody();
        System.out.println(url + "-- " + info);
        // 返回数据处理
        JsonResult result = null;
        if (StringUtils.isBlank(info)) {
            result =  new JsonResult("0001", "服务器数据返回为空");
            return -1.0 ;
        }
        String afterDat=verify(info);
        JSONObject rs= JSONObject.fromObject(afterDat);
        String blance=rs.getJSONObject("data").getString("accountBalance");
        return Double.parseDouble(blance) ;
    }




    /**
     * 单笔转账
     *
     * @param businessId
     * @return
     * @throws Exception
     * @date 2016-11-11 上午10:41:21
     * ("20155801145330130620", "郑炳", "6217906400023413538", "0.01", "",null,currencyName,"","","");
     */
    public static String singlePenTransfer(String businessId, String cardHolder, String cardNo, String tradeMoney, String remark,String code,String currencyName,String bankCode,String phone,String email) throws Exception {
        String merId = MerchantConstants.MERID;
        String version = MerchantConstants.VERSION;
        String url = MerchantConstants.SINGLETRANSFER;
        String serverPublicKey = MerchantConstants.SERVERPUBLICKEY;
        String privateKey = MerchantConstants.PRIVATEKEY;
        String terId = MerchantConstants.TERID;
        String apiKey = MerchantConstants.APIKEY;

        StringBuilder content = new StringBuilder();
        content.append("businessId=").append(businessId) ;
        content.append("&cardHolder=").append(cardHolder) ;
        content.append("&cardNo=").append(cardNo) ;
        content.append("&tradeMoney=").append(tradeMoney) ;
        content.append("&remark=").append(remark) ;
        content.append("&apiKey=").append(apiKey) ;
        if(StringUtils.isNotEmpty(code)) {
            content.append("&code=").append(code) ;
        }
        if(StringUtils.isNotBlank(currencyName)){
            content.append("&currencyName=").append(currencyName) ;
        }
        if(StringUtils.isNotBlank(currencyName) && !"CNY".equals(currencyName)) {
            content.append("&bankCode=").append(bankCode) ;
            content.append("&phone=").append(phone) ;
            content.append("&email=").append(email) ;
        }
        String hmac = MD5.MD5EncodeUTF8(content.toString());

        Map<String, String> requestmap = new HashMap<String, String>();
        requestmap.put("businessId", businessId);
        requestmap.put("cardHolder", cardHolder);
        requestmap.put("cardNo", cardNo);
        requestmap.put("tradeMoney", tradeMoney);
        requestmap.put("remark", remark);
        requestmap.put("apiKey", apiKey);
        requestmap.put("hmac", hmac);
        requestmap.put("code", code);
        requestmap.put("currencyName", currencyName);
        requestmap.put("bankCode", bankCode);
        requestmap.put("phone", phone);
        requestmap.put("email", email);
        Map<String, String> requestParam = getRequestMap(JsonUtils.objectToJson(requestmap), serverPublicKey, privateKey, version, merId, terId, "utf-8");

        // 请求
        String info = new SimpleHttpsClient().postRequest(url,requestParam, 60000).getResponseBody();
        System.out.println(url + "-- " + info);
        // 返回数据处理
        JsonResult result = null;
        if (StringUtils.isBlank(info)) {
            result =  new JsonResult("0001", "服务器数据返回为空");
            return JsonUtils.objectToJson(result) ;
        }
        return verify(info) ;
    }



    private static Map<String,String> getRequestMap(String jsonParam, String serverPublicKey, String privateKey, String version, String merId,String terId, String encoding) throws Exception {
        //System.out.println("jsonParam--- " + jsonParam);
        //用服务器公钥加密业务参数
        byte by[] = SecurityRSAPay.encryptByPublicKey(jsonParam.getBytes(encoding), Base64Local.decode(serverPublicKey));
        String encParam = Base64Local.encodeToString(by, true);
        //用商户私钥对数据签名
        byte signBy[] = SecurityRSAPay.sign(by, Base64Local.decode((privateKey)));
        String sign = Base64Local.encodeToString(signBy, true);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("encParam", encParam);
        //System.out.println("enc= " +encParam);
        maps.put("sign", sign);
        maps.put("merId", merId);
        maps.put("version", version);
        maps.put("terId",terId) ;
        return maps;
    }
    private static String verify(String info) {
        String serverPublicKey = MerchantConstants.SERVERPUBLICKEY;
        String privateKey = MerchantConstants.PRIVATEKEY;
        Map<String,String> mapParam = JsonUtils.jsonToObject(info, Map.class);
        String encParam = mapParam.get("encParam");
        String sign = mapParam.get("sign");

        JsonResult result = null;
        if(StringUtils.isBlank(encParam) || StringUtils.isBlank(sign)) {
            return info;
        }
        //验签
        boolean b = false;
        try {
            b = SecurityRSAPay.verify(Base64Local.decode(encParam), Base64Local.decode(serverPublicKey), Base64Local.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
            result=  new JsonResult("0001", "验签失败");
            return JsonUtils.objectToJson(result) ;
        }
        String paramStr = "" ;
        if (b) {// 验签通过，解密数据
            try {
                paramStr =   new String(SecurityRSAPay.decryptByPrivateKey(Base64Local.decode(encParam), Base64Local.decode(privateKey)), "utf-8");

                //md5校验
                Map<String, String> param = (Map<String, String>) JsonUtils.jsonToObject(paramStr, Map.class);
                StringBuilder content = new StringBuilder();
                content.append("details=").append(param.get("details")) ;
                content.append("&apiKey=").append(MerchantConstants.APIKEY) ;
                //param.put("apiKey", MerchantConstants.APIKEY) ;
                //JsonResult jsonResult = this.md5Validator(param);
                String hmac = param.get("hmac");
                String hmacTemp = MD5.MD5EncodeUTF8(content.toString());
                if (hmac.equals(hmacTemp)) {
                    return param.get("details");
                }else{
                    return JsonUtils.objectToJson(new JsonResult("0001", "验签失败"));
                }
            } catch (Exception e) {
                result=  new JsonResult("0001", "验签失败");
                return JsonUtils.objectToJson(result) ;
            }
        } else {
            result=  new JsonResult("0001", "验签失败");
            return JsonUtils.objectToJson(result) ;
        }

    }

}
