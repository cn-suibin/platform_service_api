package cn.merryyou.blockchain.utils;

import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.merryyou.blockchain.Block;

/**
 * Created on 2018/1/17.
 *
 * @author zlf
 * @since 1.0
 */
public class JsonUtil {
    public static String toJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
    
    public static Object toBean(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json,new TypeToken<ArrayList<Block>>(){}.getType());
    }
    
}
