package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/5/18
 */
@Setter
@Getter
public class Task {
    private int id;
    private String taskname;
    private String taskcontent;
    private String createtime;
    private String state;
    private String operauser;
    private String douser;
    private String endtime;
    private String servicebranch;

}
