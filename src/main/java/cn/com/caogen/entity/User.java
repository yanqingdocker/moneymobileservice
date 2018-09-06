package cn.com.caogen.entity;


import cn.com.caogen.util.SerializeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class User implements Serializable {
    //用户id
    private int userid;
    //用户名
    private String username;
    //用户密码
    private String password;
    //注册手机号
    private String phone;
    //用户邮箱
    private String email;
    //用户身份证号
    private String idcard;
    //出生日期
    private String birthday;
    //用户住址
    private String address;
    //创建时间
    private String createtime;
    //最后一次修改时间
    private String lasttime;
    //是否实名认证
    private int isauthentication;
    //用户级别
    private int leavel;
    //用户头像
    private  String img;
    //交易类型
    private String type;
    //交易金额
    private Double num;
    //默认账户类型
    private String defaultcount;
}
