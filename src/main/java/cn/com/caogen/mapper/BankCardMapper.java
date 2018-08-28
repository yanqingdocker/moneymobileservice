package cn.com.caogen.mapper;

import cn.com.caogen.entity.BankCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
@Component
public interface BankCardMapper {
    public void add(BankCard backCard);
    public void delete(int id);
    public List<BankCard> queryAll();
    public List<BankCard> queryCondition(Map<String,Object> parmMap);
}
