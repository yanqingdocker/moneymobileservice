package cn.com.caogen.cash.util;



import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;



@SuppressWarnings("deprecation")
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * Object 转化成json
     *
     * @param o
     *            对象值
     * @return
     */
    public static String objectToJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转化成 object
     *
     * @param json
     *            json数据
     * @param className
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> className) {
        try {
            return (T) objectMapper.readValue(json, className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转化成List
     *
     * @param json
     *            json数据
     * @param className
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String json, TypeReference<T> className) {
        try {
            return (T) objectMapper.readValue(json, className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
