package com.zyt.HttpUtil;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by chenbifeng on 16/7/25.
 */
public abstract class BeanCallBack<T> extends Callback<T> {
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType){
            ParameterizedType paramterizedType = (ParameterizedType) type;
            Type beanType = paramterizedType.getActualTypeArguments()[0];
            if (beanType == String.class){
                return (T)response.body().string();
            }else{
                return new Gson().fromJson(response.body().string(),beanType);
            }
        }else {
            return (T) response;
        }
    }
}
