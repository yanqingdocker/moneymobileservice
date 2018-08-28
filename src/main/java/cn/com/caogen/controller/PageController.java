package cn.com.caogen.controller;

import cn.com.caogen.entity.Count;
import cn.com.caogen.entity.Operation;
import cn.com.caogen.service.BankCardServiceImpl;
import cn.com.caogen.service.CountServiceImpl;
import cn.com.caogen.service.OperaServiceImpl;
import cn.com.caogen.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:huyanqing
 * Date:2018/4/19
 */
@Controller
public class PageController {
    private static Logger logger = LoggerFactory.getLogger(PageController.class);
    @Autowired
    private OperaServiceImpl operaService;
    @Autowired
    private CountServiceImpl countServiceImpl;
    @Autowired
    private BankCardServiceImpl bankCardService;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    private int userid;
    /**
     * 支付后回调接口
     */
    @RequestMapping("/goback")
    @Transactional //开启事务，两个账户都更新成功是才算成功
    public String goBack(HttpServletRequest request) {
        logger.info("goBack start");
        Lock lock = new ReentrantLock();
        lock.lock();//加锁
        try {
            String userid=request.getParameter("userid");
            if(StringUtil.checkStrs(userid)&&userid.equals(String.valueOf(JedisUtil.getUser(request).getUserid()))){
                String cardid = request.getParameter("cardid");
                Double num=Double.parseDouble(request.getParameter("blance"))/100+Double.parseDouble(request.getParameter("blance"))%1;
                Count personCount = countServiceImpl.queryById(cardid);
                //更新个人账户
                countServiceImpl.updateCount(cardid, personCount.getBlance() + num, null,null);
                Operation operation=new Operation();
                operation.setOperaType(ConstantUtil.SERVICETYPE_RECHARGE);
                operation.setCountid(personCount.getCardId());
                operation.setSnumber(SerialnumberUtil.Getnum());
                operation.setOi(ConstantUtil.MONEY_IN);
                operation.setCountType(personCount.getCountType());
                operation.setServicebranch(ConstantUtil.SYSTEM);
                operation.setNum(num);
                operation.setOperaTime(DateUtil.getTime());
                operation.setOperaIp(IpUtil.getIpAddr(request));
                operaService.add(operation);
            }

        } catch (Exception e) {
            return "CN/more/trade_list";
        } finally {
            lock.unlock();
        }
        return "CN/more/trade_list";

    }

    //    中文版
    /**
     * 访问index界面
     *
     * @returnmy/my_account
     */
    @RequestMapping("/CN/index")
    public String CNindex() {
        return "CN/index";
    }

    /**
     * 访问注册页面
     *
     * @return
     */
    @RequestMapping("/CN/register")
    public String CNregister() {
        return "CN/register";
    }

    /**
     * 访问登录页面
     *
     * @return
     */
    @RequestMapping("/CN/login")
    public String CNlogin(HttpServletRequest httpServletRequest) {
        if(JedisUtil.getUser(httpServletRequest) !=null){
            return "CN/index";
        }
        return "CN/login";
    }

    /**
     * 访问忘记密码界面
     *
     * @return
     */
    @RequestMapping("/CN/find_psw")
    public String CNfind_psw() {
        return "CN/find_psw";
    }

    /**
     * 账户充值
     *
     * @return
     */
    @RequestMapping("/CN/main/recharge_form")
    public String CNrecharge_form() {
        return "CN/main/recharge_form";
    }
    /**
     * 充值
     *
     * @return
     */
    @RequestMapping("/CN/main/recharge")
    public String CNmainrecharge() {
        return "CN/main/recharge";
    }
    /**
     * 我的资料
     *
     * @return
     */
    @RequestMapping("/CN/my/my_account")
    public String CNmyaccount() {
        return "CN/my/my_account";
    }

    /**
     * 账户认证/基本资料
     *
     * @return
     */
    @RequestMapping("/CN/my/authentication")
    public String CNauthentication() {
        return "CN/my/authentication";
    }
    /**
     * 我的余额
     *
     * @return
     */
    @RequestMapping("/CN/my/my_money")
    public String CNmy_money() {
        return "CN/my/my_money";
    }
    /**
     * 登录密码
     *
     * @return
     */
    @RequestMapping("/CN/my/login_psw")
    public String CNlogin_psw() {
        return "CN/my/login_psw";
    }
    /**
     * 资金密码
     *
     * @return
     */
    @RequestMapping("/CN/my/pay_psw")
    public String CNpay_psw() {
        return "CN/my/pay_psw";
    }
    /**
     * 银行卡绑定
     *
     * @return
     */
    @RequestMapping("/CN/my/bank_card")
    public String CNbank_card() {
        return "CN/my/bank_card";
    }
    /**
     * 支付宝微信/绑定
     *
     * @return
     */
    @RequestMapping("/CN/my/bind_account")
    public String CNbind_account() {
        return "CN/my/bind_account";
    }
    /**
     * 更多--生活缴费
     *
     * @return
     */
    @RequestMapping("/CN/more/life_payment")
    public String CNlife_payment() {
        return "CN/more/life_payment";
    }
    /**
     * 生活缴费--话费充值
     *
     * @return
     */
    @RequestMapping("/CN/more/phone_charge")
    public String CNphone_charge() {
        return "CN/more/phone_charge";
    }
    /**
     * 生活缴费--水费代缴
     *
     * @return
     */
    @RequestMapping("/CN/more/water_charge")
    public String CNwater_charge() {
        return "CN/more/water_charge";
    }
    /**
     * 生活缴费--电费代缴
     *
     * @return
     */
    @RequestMapping("/CN/more/ele_charge")
    public String CNele_charge() {
        return "CN/more/ele_charge";
    }
    /**
     * 我的消息--写信
     *
     * @return
     */
    @RequestMapping("/CN/my/message")
    public String CNmessage() {
        return "CN/my/message";
    }
    /**
     * 我的消息--收件箱
     *
     * @return
     */
    @RequestMapping("/CN/my/inbox")
    public String CNinbox() {
        return "CN/my/inbox";
    }
    /**
     * 我的消息--发件箱
     *
     * @return
     */
    @RequestMapping("/CN/my/outbox")
    public String CNoutbox() {
        return "CN/my/outbox";
    }

    /**
     * 货币兑换
     *
     * @return
     */
    @RequestMapping("/CN/currency_exchange/exchange")
    public String CNexchange() {
        return "CN/currency_exchange/exchange";
    }
    /**
     * 交易明细列表
     * @return
     */
    @RequestMapping("/CN/currency_exchange/exchange_list")
    public String CNexchange_list() {
        return "CN/currency_exchange/exchange_list"; }
    /**
     * 汇率列表
     * @return
     */
    @RequestMapping("/CN/currency_exchange/exchange_rate_list")
    public String CNexchange_rate_list() {
        return "CN/currency_exchange/exchange_rate_list"; }
    /**
     * 转账
     * @return
     */
    @RequestMapping("/CN/main/transfer_accounts")
    public String CNtransfer_accounts() {
        return "CN/main/transfer_accounts"; }
    /**
     * 提现
     * @return
     */
    @RequestMapping("/CN/main/trans_cash")
    public String CNtrans_cash() {
        return "CN/main/trans_cash"; }
    /**
     * 提现---外币提现
     * @return
     */
    @RequestMapping("/CN/main/foreign_currency")
    public String foreign_currency() {
        return "CN/main/foreign_currency"; }
    /**
     * 提现---从银行提现
     * @return
     */
    @RequestMapping("/CN/main/pos_Bank")
    public String CNpos_Bank() {
        return "CN/main/pos_Bank"; }
    /**
     * 提现---从微信提现
     * @return
     */
    @RequestMapping("/CN/main/pos_WeChat")
    public String CNpos_WeChat() {
        return "CN/main/pos_WeChat"; }

    /**
     * 提现---从支付宝提现
     * @return
     */
    @RequestMapping("/CN/main/pos_Alipay")
    public String CNpos_Alipay() {
        return "CN/main/pos_Alipay"; }
    /**
     * 提现---现金提现
     * @return
     */
    @RequestMapping("/CN/main/pos_cash")
    public String CNpos_cash() {
        return "CN/main/pos_cash"; }
    /**
     * 提现---比特币提现
     * @return
     */
    @RequestMapping("/CN/main/pos_bitcoin")
    public String CNpos_bitcoin() {
        return "CN/main/pos_bitcoin"; }

    /**
     * 无卡取现
     * @return
     */
    @RequestMapping("/CN/nonCardCash/cash_in")
    public String CNnonCardCash() {
        return "CN/nonCardCash/cash_in"; }

    /**
     * 智慧理财
     * @return
     */
    @RequestMapping("/CN/more/financial")
    public String CNfinancial() {
        return "CN/more/financial"; }

    /**
     * 交易记录
     * @return
     */
    @RequestMapping("/CN/more/trade_list")
    public String CNtrade_list() {
        return "CN/more/trade_list"; }
    /**
     * 历史交易记录
     * @return
     */
    @RequestMapping("/CN/more/trade_history")
    public String CNtrade_history() {
        return "CN/more/trade_history"; }

    /**
     * 活动记录
     * @return
     */
    @RequestMapping("/CN/more/active_list")
    public String CNactive_list() {
        return "CN/more/active_list"; }
    /**
     * 修改手机号
     * @return
     */
    @RequestMapping("/CN/my/change_phone")
    public String CNchange_phone() {
        return "CN/my/change_phone"; }
    /**
     * 我的借贷
     * @return
     */
    @RequestMapping("/CN/my/my_loan")
    public String CNmy_loan() {
        return "CN/my/my_loan"; }

    //    以下为英文版
    /**
     * 访问index界面
     *
     * @returnmy/my_account
     */
    @RequestMapping("/EN/index")
    public String ENindex() {
        return "EN/index";
    }

    /**
     * 访问注册页面
     *
     * @return
     */
    @RequestMapping("/EN/register")
    public String ENregister() {
        return "EN/register";
    }

    /**
     * 访问登录页面
     *
     * @return
     */
    @RequestMapping("/EN/login")
    public String ENlogin(HttpServletRequest httpServletRequest) {
        if(JedisUtil.getUser(httpServletRequest) !=null){
            return "EN/index";
        }
        return "EN/login";
    }

    /**
     * 访问忘记密码界面
     *
     * @return
     */
    @RequestMapping("/EN/find_psw")
    public String ENfind_psw() {
        return "EN/find_psw";
    }

    /**
     * 账户充值
     *
     * @return
     */
    @RequestMapping("/EN/main/recharge_form")
    public String ENrecharge_form() {
        return "EN/main/recharge_form";
    }
    /**
     * 充值
     *
     * @return
     */
    @RequestMapping("/EN/main/recharge")
    public String ENmainrecharge() {
        return "EN/main/recharge";
    }
    /**
     * 我的资料
     *
     * @return
     */
    @RequestMapping("/EN/my/my_account")
    public String ENmyaccount() {
        return "EN/my/my_account";
    }

    /**
     * 账户认证/基本资料
     *
     * @return
     */
    @RequestMapping("/EN/my/authentication")
    public String ENauthentication() {
        return "EN/my/authentication";
    }
    /**
     * 我的余额
     *
     * @return
     */
    @RequestMapping("/EN/my/my_money")
    public String ENmy_money() {
        return "EN/my/my_money";
    }
    /**
     * 登录密码
     *
     * @return
     */
    @RequestMapping("/EN/my/login_psw")
    public String ENlogin_psw() {
        return "EN/my/login_psw";
    }
    /**
     * 资金密码
     *
     * @return
     */
    @RequestMapping("/EN/my/pay_psw")
    public String ENpay_psw() {
        return "EN/my/pay_psw";
    }
    /**
     * 银行卡绑定
     *
     * @return
     */
    @RequestMapping("/EN/my/bank_card")
    public String ENbank_card() {
        return "EN/my/bank_card";
    }
    /**
     * 支付宝微信/绑定
     *
     * @return
     */
    @RequestMapping("/EN/my/bind_account")
    public String ENbind_account() {
        return "EN/my/bind_account";
    }
    /**
     * 更多--生活缴费
     *
     * @return
     */
    @RequestMapping("/EN/more/life_payment")
    public String ENlife_payment() {
        return "EN/more/life_payment";
    }
    /**
     * 生活缴费--话费充值
     *
     * @return
     */
    @RequestMapping("/EN/more/phone_charge")
    public String ENphone_charge() {
        return "EN/more/phone_charge";
    }
    /**
     * 生活缴费--水费代缴
     *
     * @return
     */
    @RequestMapping("/EN/more/water_charge")
    public String ENwater_charge() {
        return "EN/more/water_charge";
    }
    /**
     * 生活缴费--电费代缴
     *
     * @return
     */
    @RequestMapping("/EN/more/ele_charge")
    public String ENele_charge() {
        return "EN/more/ele_charge";
    }
    /**
     * 我的消息--写信
     *
     * @return
     */
    @RequestMapping("/EN/my/message")
    public String ENmessage() {
        return "EN/my/message";
    }
    /**
     * 我的消息--收件箱
     *
     * @return
     */
    @RequestMapping("/EN/my/inbox")
    public String ENinbox() {
        return "EN/my/inbox";
    }
    /**
     * 我的消息--发件箱
     *
     * @return
     */
    @RequestMapping("/EN/my/outbox")
    public String ENoutbox() {
        return "EN/my/outbox";
    }

    /**
     * 货币兑换
     *
     * @return
     */
    @RequestMapping("/EN/currency_exchange/exchange")
    public String ENexchange() {
        return "EN/currency_exchange/exchange";
    }
    /**
     * 交易明细列表
     * @return
     */
    @RequestMapping("/EN/currency_exchange/exchange_list")
    public String ENexchange_list() {
        return "EN/currency_exchange/exchange_list"; }
    /**
     * 汇率列表
     * @return
     */
    @RequestMapping("/EN/currency_exchange/exchange_rate_list")
    public String ENexchange_rate_list() {
        return "EN/currency_exchange/exchange_rate_list"; }
    /**
     * 转账
     * @return
     */
    @RequestMapping("/EN/main/transfer_accounts")
    public String ENtransfer_accounts() {
        return "EN/main/transfer_accounts"; }
    /**
     * 提现
     * @return
     */
    @RequestMapping("/EN/main/trans_cash")
    public String ENtrans_cash() {
        return "EN/main/trans_cash"; }
    /**
     * 提现---外币提现
     * @return
     */
    @RequestMapping("/EN/main/foreign_currency")
    public String ENforeign_currency() {
        return "EN/main/foreign_currency"; }
    /**
     * 提现---从银行提现
     * @return
     */
    @RequestMapping("/EN/main/pos_Bank")
    public String ENpos_Bank() {
        return "EN/main/pos_Bank"; }
    /**
     * 提现---从微信提现
     * @return
     */
    @RequestMapping("/EN/main/pos_WeChat")
    public String ENpos_WeChat() {
        return "EN/main/pos_WeChat"; }

    /**
     * 提现---从支付宝提现
     * @return
     */
    @RequestMapping("/EN/main/pos_Alipay")
    public String ENpos_Alipay() {
        return "EN/main/pos_Alipay"; }
    /**
     * 提现---现金提现
     * @return
     */
    @RequestMapping("/EN/main/pos_cash")
    public String ENpos_cash() {
        return "EN/main/pos_cash"; }
    /**
     * 提现---比特币提现
     * @return
     */
    @RequestMapping("/EN/main/pos_bitcoin")
    public String ENpos_bitcoin() {
        return "EN/main/pos_bitcoin"; }

    /**
     * 无卡取现
     * @return
     */
    @RequestMapping("/EN/nonCardCash/cash_in")
    public String ENnonCardCash() {
        return "EN/nonCardCash/cash_in"; }

    /**
     * 智慧理财
     * @return
     */
    @RequestMapping("/EN/more/financial")
    public String ENfinancial() {
        return "EN/more/financial"; }

    /**
     * 交易记录
     * @return
     */
    @RequestMapping("/EN/more/trade_list")
    public String ENtrade_list() {
        return "EN/more/trade_list"; }
    /**
     * 历史交易记录
     * @return
     */
    @RequestMapping("/EN/more/trade_history")
    public String ENtrade_history() {
        return "EN/more/trade_history"; }

    /**
     * 活动记录
     * @return
     */
    @RequestMapping("/EN/more/active_list")
    public String ENactive_list() {
        return "EN/more/active_list"; }
    /**
     * 修改手机号
     * @return
     */
    @RequestMapping("/EN/my/change_phone")
    public String ENchange_phone() {
        return "EN/my/change_phone"; }
    /**
     * 我的借贷
     * @return
     */
    @RequestMapping("/EN/my/my_loan")
    public String ENmy_loan() {
        return "EN/my/my_loan"; }





}
