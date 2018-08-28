package cn.com.caogen.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMessage {
    private String code;
    private String message;
    public ResponseMessage(String code,String message){
        this.code=code;
        this.message=message;
    }
    public ResponseMessage(String code){
        this.code=code;
    }
}
