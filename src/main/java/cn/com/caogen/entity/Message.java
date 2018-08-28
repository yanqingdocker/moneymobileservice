package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/4/25
 */
@Setter
@Getter
public class Message{
    //消息id
    private int id;
    //信息标题
    private String title;
    //信息内容
    private String content;
    //创建信息时间
    private String createTime;
    //信息人
    private String username;
    //信息类型
    private int messagetype;
    //是否标记
    private int Visiable;
    //操作人
    private int userid;
}
