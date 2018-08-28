package cn.com.caogen.externIsystem.service;

import cn.com.caogen.externIsystem.mode.HttpSendResult;
import cn.com.caogen.externIsystem.mode.JsonResult;
import cn.com.caogen.externIsystem.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 演示demo 以下demo仅供参考，商户可根据实际需要自行修改
 */

@Controller
@RequestMapping("/")
public class PayService {

	PrintWriter out = null;
	HttpServletResponse response =null;
	static String encoding = "UTF-8";

	/**
	 * 跳转到支付网关（PC和H5网关使用此接口）
	 * @Description:
	 * void
	 */

	public static String orderPay(Map<String,Object> map) {
		String htmlBuild="";
		try {
			String id = "" + System.currentTimeMillis();
			map.put("businessOrdid", id);
			map.put("orderName", "线上充值");
			map.put("merId", Config.MERID);
			map.put("terId", Config.TERID);
			map.put("tradeMoney", map.get("blance"));
			map.put("selfParam", "aa");
			map.put("payType", 1000); // 1000默认支持所有支付方式
			map.put("appSence",1001 );
			String cardid=map.get("cardid").toString();
			String blance=map.get("blance").toString();
			String userid=map.get("userid").toString();
			String sessionid=map.get("sessionid").toString();
			map.put("syncURL", "http://www.nfgjbank.com/goback?cardid="+cardid+"&&blance="+blance+"&&userid="+userid+"&&sessionid="+sessionid);
			map.put("asynURL", "http://www.nfgjbank.com/goback?cardid="+cardid+"&&blance="+blance+"&&userid="+userid+"&&sessionid="+sessionid);

			String json = GsonUtil.toJson(map);
			// 获取请求的参数
			Map<String, String> paramMap = getRequestMap(json, Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY, Config.VERSION, Config.MERID);

			htmlBuild = buildRequest(paramMap, "post", "gatewayPay",Config.PAYURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlBuild;
	}


	/**
	 * 获取二维码地址
	 * @Description:
	 * @param response
	 * @param request
	 * void
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/orderPaySweepCode")
	public void orderPaySweepCode(HttpServletResponse response,HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();

			String tradeMoney = request.getParameter("tradeMoney");// 交易金额
			String payType = request.getParameter("payType");// 支付方式
			String appSence = request.getParameter("appSence");// 应用场景
			String asynURL = request.getParameter("asynURL");// 异步地址
			String businessOrdid = "" + System.currentTimeMillis();

			// 拼接请求参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("businessOrdid", "123456789");
			map.put("orderName", "测试支付");
			map.put("merId", Config.MERID);
			map.put("terId", Config.TERID);
			map.put("tradeMoney", tradeMoney);
			map.put("appSence", appSence);
			map.put("selfParam", "自定义参数");// 商户自己传递
			map.put("payType", payType);
			map.put("asynURL", asynURL);

			String json = GsonUtil.toJson(map);
			// 获取请求的参数
			Map<String, String> reqParam = getRequestMap(json, Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY, Config.VERSION, Config.MERID);

			SimpleHttpsClient httpClient = new SimpleHttpsClient();
			HttpSendResult result = null;
			String respTxt = "";
			try {
				result = httpClient.postRequest(Config.QRCODEURL, reqParam, 30 * 000);
				respTxt = result.getResponseBody();

				System.out.println("服务器返回:" + respTxt);
				if (result.getStatus() != 200) {
					// 请求服务器失败， 商户出错处理
					return;
				}

				// 服务器没有返回数据或返回异常
				if (StringUtils.isEmpty(respTxt) || respTxt.contains("</html>")) {
					// 商户出错处理
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
				// 商户出错处理
				return;
			}
			Map<String, String> respMap = GsonUtil.fromJson(respTxt, Map.class);
			//验签和解密
			JsonResult verifyResult = verifyAndDecrypt(respMap.get("encParam"), respMap.get("sign"), Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY);
			if (!"1000".equals(verifyResult.getRespCode())) {
				//商户错误处理
				out.print(GsonUtil.toJson(verifyResult));
				return;
			}
			try {
				resultMap = GsonUtil.fromJson(verifyResult.getData().toString(), Map.class);
			} catch (Exception e) {
				System.out.println("转换json格式错误");
				// 商户错误处理
				return;
			}
			String code = resultMap.get("respCode"); // 返回码返回1000表示成功。当respCode为1000时，订单数据才有效。
			if (!"1000".equals(code)) {
				// 商户错误处理
				out.print(GsonUtil.toJson(resultMap));
				return;
			}
			out.print(GsonUtil.toJson(resultMap));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询订单状态
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/queryOrderInfo")
	public void queryOrderInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();

			String businessOrdid = request.getParameter("businessOrdid");
			Map<String, String> map = new HashMap<String, String>();
			map.put("businessOrdid", "123456789");
			String json = GsonUtil.toJson(map);
			// 获取请求的参数
			Map<String, String> reqParam = getRequestMap(json, Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY, Config.VERSION, Config.MERID);

			SimpleHttpsClient httpClient= new SimpleHttpsClient();
			HttpSendResult result = null;
			String respTxt ="";
			try {
				result = httpClient.postRequest(Config.QUERYURL, reqParam, 30*000);
				respTxt = result.getResponseBody();
				if(result.getStatus() != 200){
					//请求服务器失败、商户出错处理
					return ;
				}
				//服务器没有返回数据或返回异常
				if(StringUtils.isEmpty(respTxt) || respTxt.contains("</html>")){
					//商户出错处理
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				//商户出错处理
				return ;
			}
			Map<String, String> respMap =GsonUtil.fromJson(respTxt, Map.class);
			//验签和解密
			JsonResult verifyResult = verifyAndDecrypt(respMap.get("encParam"), respMap.get("sign"), Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY);
			if (!"1000".equals(verifyResult.getRespCode())) {
				//商户错误处理
				out.print(GsonUtil.toJson(verifyResult));
				return;
			}
			try {
				resultMap = GsonUtil.fromJson(verifyResult.getData().toString(), Map.class);
			} catch (Exception e) {
				System.out.println("转换json格式错误");
				// 商户错误处理
				return;
			}
			System.out.println("服务器返回数据："+resultMap.toString());
			String code = resultMap.get("respCode"); // 返回码返回1000表示成功。当respCode为1000时，订单数据才有效。
			if (!"1000".equals(code)) {
				// 商户错误处理
				out.print(GsonUtil.toJson(resultMap));
				return;
			}
			out.print(GsonUtil.toJson(resultMap));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 异步通知解析(同步类似)
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/synMerchant")
	public void synMerchant(HttpServletResponse response,HttpServletRequest request) throws Exception {
		out = response.getWriter();
		String encParam = request.getParameter("encParam");
		String merId = request.getParameter("merId");
		String version = request.getParameter("version");
		String sign = request.getParameter("sign");

		Map<String, String> resultMap = new HashMap<String, String>();
		//验签和解密
		JsonResult verifyResult = verifyAndDecrypt(encParam, sign, Config.SERVER_PUBLIC_KEY, Config.PRIVATE_KEY);
		if (!"1000".equals(verifyResult.getRespCode())) {
			//商户错误处理
			return;
		}
		try {
			resultMap = GsonUtil.fromJson(verifyResult.getData().toString(), Map.class);
		} catch (Exception e) {
			System.out.println("转换json格式错误");
			// 商户错误处理
			return;
		}

		String orderId = resultMap.get("orderId"); // 商户订单号
		String payOrderId = resultMap.get("payOrderId"); // 支付订单号
		String order_state = resultMap.get("order_state"); // 订单状态
		String money = resultMap.get("money"); // 交易金额
		String payReturnTime = resultMap.get("payReturnTime"); // 付款时间
		String selfParam = resultMap.get("selfParam"); // 自定义参数
		String payType = resultMap.get("payType"); // 支付方式
		String payTypeDesc = resultMap.get("payTypeDesc"); // 支付方式描述

		/************** 商户业务开始 **************/
		// 注意避免重复通知导致业务出现异常

		/************** 商户业务结束 **************/

		out.print("SUCCESS"); // 商户记得一定要回写
		return;
	}

	/**
	 * 组装请求参数
	 */
	private static Map<String, String> getRequestMap(String jsonParam,String serverPublicKey,String privateKey,String version,String merId) throws Exception{
		//服务器公钥加密
		byte by[] = SecurityRSAPay.encryptByPublicKey(jsonParam.getBytes(encoding), Base64Local.decode(serverPublicKey));
		String baseStrDec = Base64Local.encodeToString(by, true);

		//商户自己的私钥签名
		byte signBy[] = SecurityRSAPay.sign(by, Base64Local.decode(privateKey));
		String sign = Base64Local.encodeToString(signBy, true);

		//组装请求参数
		Map<String,String>  map = new HashMap<String, String>();
		map.put("encParam", baseStrDec);
		map.put("merId", merId);
		map.put("sign",sign);
		map.put("version",  version);
		return map;
	}

	/**
	 * 验签和解密
	 */
	private JsonResult verifyAndDecrypt(String encParam,String sign,String serverPublicKey,String privateKey){

		if (StringUtils.isEmpty(encParam) || StringUtils.isEmpty(sign)) {
			return new JsonResult("0001", "数据异常");
		}
		// 服务器公钥验签
		boolean flag;
		try {
			flag = SecurityRSAPay.verify(Base64Local.decode(encParam),Base64Local.decode(serverPublicKey),Base64Local.decode(sign));
			// 验签失败
			if (!flag) {
				// 商户出错处理
				System.out.println("验签失败");
				return new JsonResult("0001", "验签失败");
			}
			// 商户自己私钥解密
			String data = new String(SecurityRSAPay.decryptByPrivateKey(Base64Local.decode(encParam),Base64Local.decode(privateKey)), "utf-8");
			return new JsonResult("1000", "成功", data);

		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult("0001", "系统异常");
		}

	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * @param sParaTemp 请求参数数组
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   buildRequest(Map<String, String> sParaTemp, String strMethod,String strButtonName, String serverUrl) {
		// 待请求参数数组
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());

		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<form id=\"paysubmit\" name=\"paysubmit\" action=\""
				+ serverUrl + "\" method=\"" + strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sParaTemp.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['paysubmit'].submit();</script>");
		return sbHtml.toString();
	}


}
