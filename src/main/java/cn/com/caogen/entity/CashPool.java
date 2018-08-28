package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/5/17
 */
@Setter
@Getter
public class CashPool {
    private int id;
    private String countid;
    private String counttype;
    private Double blance;
    private String lasttime;
    private String servicebranch;
}
