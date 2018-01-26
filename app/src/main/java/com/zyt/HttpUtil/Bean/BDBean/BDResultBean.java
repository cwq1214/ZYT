package com.zyt.HttpUtil.Bean.BDBean;

import java.util.List;

/**
 * Created by chenbifeng on 16/7/26.
 */
public class BDResultBean<T> {
    public int status;
    public String message;
    public int total;
    public List<T> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "BDResultBean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", total=" + total +
                ", results=" + results +
                '}';
    }
}
