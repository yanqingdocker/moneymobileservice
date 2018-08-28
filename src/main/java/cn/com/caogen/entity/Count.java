package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/4/12
 */
@Setter
@Getter
public class Count {
    //主键id
    private int id;
    //账户id
    private String cardId;
    //账户类型
    private String countType;
    //是否启用
    private String state;
    //账户主人id
    private String userId;
    //账户余额
    private double blance;
    //账户支付密码
    private String payPwd;
    //创建账户时间
    private String createTime;
    //校验位
    private String checkCode;
}
