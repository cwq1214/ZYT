package com.zyt.base;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class BaseJSON<T> {
    String version;
    T data;
    String forWorker;
    String ret;
    int code;
    String forUser;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getForWorker() {
        return forWorker;
    }

    public void setForWorker(String forWorker) {
        this.forWorker = forWorker;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }
}
