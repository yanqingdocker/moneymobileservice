package cn.com.caogen.service;

import cn.com.caogen.entity.BankCard;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
public interface IBankCardService {

    public void add(Map<String, String> parmMap);

    public List<BankCard> queryCondition(Map<String, Object> parmMap);

    public void delete(int id);


}
