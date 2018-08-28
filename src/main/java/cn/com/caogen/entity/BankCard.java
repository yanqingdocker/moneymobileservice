package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
@Setter
@Getter
public class BankCard {
    //主键id
    private int id;
    //银行卡号
    private String bankCard;
    //银行类型
    private String bankType;
    //身份证号
    private String idCard;
    //姓名
    private String username;
    //银行预留电话
    private String phone;
    //开户地址
    private String cardAddress;
    //持有卡用户id
    private int userId;
    //创建时间
    private String createTime;
}
