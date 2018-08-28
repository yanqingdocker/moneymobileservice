package cn.com.caogen.util;

import cn.com.caogen.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * author:huyanqing
 * Date:2018/4/20
 */
public class DataMonitor {

    /**
     * 测试验证
     * @param user
     * @return
     */
    public static boolean check(User user){
        String code=getValiateCode(user,"phone");
        if(user.getPhone().equals(code)){
           return true;
        }else{
            return false;
        }
    }

    /**
     * 通过对象得到唯一的校验码
     * @param obj 验证的对象
     * @param args 忽略对象的某几个字段
     * @return
     */
    public static String getValiateCode(Object obj,String... args){
        String code="";
        try{
            StringBuffer stringBuffer=new StringBuffer();
            Field[] fields=obj.getClass().getDeclaredFields();
            List<Field> list=new ArrayList<Field>();
            for(Field field:fields){
                list.add(field);
            }
            ListIterator<Field> iterable=list.listIterator();
            while (iterable.hasNext()){
                String name=iterable.next().getName();
                for(String str:args){
                    if(name.equals(str)){
                        iterable.remove();
                        break;
                    }
                }
            }
            for(Field field:list){
                String str=field.getName();
                String firstLetter = str.substring(0, 1).toUpperCase();
                String getter = "get" + firstLetter + str.substring(1);
                Method method = obj.getClass().getMethod(getter, new Class[] {});
                Object value = method.invoke(obj, new Object[] {});
                stringBuffer.append(value);
            }
            code=""+stringBuffer.toString().hashCode();
        }catch (Exception e){

        }
        return code;
    }

    public static String reSet(String str){
        JSONObject jsonObject=JSONArray.fromObject(str).getJSONObject(0).getJSONObject("USDCNY");
        Set<String> keys=jsonObject.keySet();
        for(String key:keys){
            if(key.equals("date")){
                jsonObject.element(key,DateUtil.getDate());
            }
        }
        JSONObject jsonObject1=JSONArray.fromObject(str).getJSONObject(0);
        keys=jsonObject1.keySet();
        for(String key:keys){
            if(key.equals("USDCNY")){
                jsonObject1.element(key,jsonObject);
            }
        }
        return jsonObject1.toString();
    }
}
