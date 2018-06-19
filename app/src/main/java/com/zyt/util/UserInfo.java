package com.zyt.util;

import com.orhanobut.hawk.Hawk;

/**
 * Created by chenweiqi on 2018/5/31.
 */

public class UserInfo {
    public static void setUserLocation(String location){
        Hawk.put("lc",location);
    }

    public static String getUserLocation(){
        return Hawk.get("lc",null);
    }

    public static boolean isLogin(){
        return Hawk.get("token",null)!=null;
    }

    public static void setToken(String token){
        Hawk.put("token",token);
    }

    public static String getToken(){
        return Hawk.get("token");
    }
}
