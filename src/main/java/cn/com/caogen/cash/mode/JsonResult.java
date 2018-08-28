package cn.com.caogen.cash.mode;

import java.io.Serializable;

public class JsonResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String respCode;

    private String message;

    private Object data;

    private String sign;

    public JsonResult(){}

    public JsonResult(String respCode, String message) {
        super();
        this.respCode = respCode;
        this.message = message;
    }
    public JsonResult(String respCode, String message, Object data) {
        this(respCode,message) ;
        this.data = data;
    }
    public JsonResult(String respCode, String message, Object data,  String sign) {
        this(respCode,message,data);
        this.sign = sign;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
