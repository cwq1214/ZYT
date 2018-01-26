package com.zyt.HttpUtil.Bean;

/**
 * Created by chenbifeng on 16/7/25.
 */
public class ServerBean<T> {
    public String ret;
    public String forUser;
    public String version;
    public String forWorker;
    public T data;
    public int code;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getForWorker() {
        return forWorker;
    }

    public void setForWorker(String forWorker) {
        this.forWorker = forWorker;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ServerBean{" +
                "ret='" + ret + '\'' +
                ", forUser='" + forUser + '\'' +
                ", version='" + version + '\'' +
                ", forWorker='" + forWorker + '\'' +
                ", data=" + data +
                ", code=" + code +
                '}';
    }
}
