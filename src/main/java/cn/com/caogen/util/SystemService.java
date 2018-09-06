package cn.com.caogen.util;


import org.json.JSONObject;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;


public class SystemService {
    public static void send(String title,String content,String account){
       /* XingeApp.pushAccountAndroid(
                2100310004, "fc017e581c8cf0cfd2b6b02cc66fae5c", title, content, account);
        XingeApp.pushAccountIos(Long.parseLong("2200309966"),"bc41c3e6bf203a808a3698e2c0294006",content,account,XingeApp.IOSENV_DEV);
        ClickAction action = new ClickAction();
        action.setActionType(ClickAction.TYPE_ACTIVITY);
        action.setActivity("cn.com.caogen.moneysystem.BillqueryActivity");*/
        SystemService service=new SystemService();
        service.PushSingleAccountIOS(content,account);
        service.PushSingleAccountAndroid(title,content,account);

    }


    //下发单个账号到IOS
    private  void PushSingleAccountIOS(String content,String account) {
        xinge = new XingeApp(Long.parseLong("2200309966"), "bc41c3e6bf203a808a3698e2c0294006");
        MessageIOS  message = new MessageIOS ();
        message.setSound("default");
        message.setType(MessageIOS.TYPE_APNS_NOTIFICATION);
        message.setCategory("INVITE_CATEGORY");
        message.setExpireTime(86400);
        message.setAlert(content);
        message.setBadge(1);
        JSONObject ret = xinge.pushSingleAccount(0,account,message,XingeApp.IOSENV_DEV);

    }

    //下发单个账号到安卓
    private void PushSingleAccountAndroid(String title,String content,String account) {
        xinge = new XingeApp(Long.parseLong("2100310004"), "fc017e581c8cf0cfd2b6b02cc66fae5c");
        Message mess = new Message();
        mess.setType(Message.TYPE_NOTIFICATION);
        mess.setTitle(title);
        mess.setContent(content);
        mess.setExpireTime(86400);
        mess.setStyle(new Style(0,1,1,0,0));
        ClickAction action = new ClickAction();
        action.setActionType(ClickAction.TYPE_ACTIVITY);
        action.setActivity("cn.com.caogen.moneysystem.BillqueryActivity");
        mess.setAction(action);
        JSONObject ret = xinge.pushSingleAccount(0,account,mess);

    }

    private XingeApp xinge;
}
