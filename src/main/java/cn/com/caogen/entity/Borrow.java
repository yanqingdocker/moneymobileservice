package cn.com.caogen.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * author:huyanqing
 * Date:2018/6/7
 */
@Setter
@Getter
public class Borrow {
    private int id;
    private String snumber;
    private String warrantor;
    private String borrowerphone;
    private String borrower;
    private Double num;
    private String moneytype;
    private int term;
    private String createtime;
    private int status;
    private String servicebranch;
    private String operauser;

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", snumber='" + snumber + '\'' +
                ", warrantor='" + warrantor + '\'' +
                ", borrowerphone='" + borrowerphone + '\'' +
                ", borrower='" + borrower + '\'' +
                ", num=" + num +
                ", moneytype='" + moneytype + '\'' +
                ", term=" + term +
                ", createtime='" + createtime + '\'' +
                ", status=" + status +
                ", servicebranch='" + servicebranch + '\'' +
                ", operauser='" + operauser + '\'' +
                '}';
    }
}
