package cn.com.caogen.service;

import cn.com.caogen.entity.BankCard;
import cn.com.caogen.mapper.BankCardMapper;
import cn.com.caogen.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
@Service
public class BankCardServiceImpl implements IBankCardService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Override
    public void add(Map<String, String> parmMap) {
        BankCard backCard = new BankCard();
        backCard.setIdCard(parmMap.get("idcard"));
        backCard.setBankCard(parmMap.get("bankcard"));
        backCard.setBankType(parmMap.get("banktype"));
        backCard.setCardAddress(parmMap.get("address"));
        backCard.setUsername(parmMap.get("username"));
        backCard.setPhone(parmMap.get("phone"));
        backCard.setUserId(Integer.parseInt(parmMap.get("userid")));
        backCard.setCreateTime(DateUtil.getTime());
        bankCardMapper.add(backCard);


    }

    @Override
    public List<BankCard> queryCondition(Map<String, Object> parmMap) {
        return bankCardMapper.queryCondition(parmMap);
    }

    @Override
    public void delete(int id) {
        bankCardMapper.delete(id);

    }
}
