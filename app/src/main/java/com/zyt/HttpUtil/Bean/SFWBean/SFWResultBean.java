package com.zyt.HttpUtil.Bean.SFWBean;

/**
 * Created by chenbifeng on 16/7/27.
 */
public class SFWResultBean<T> {
    public String searchtype;
    public T loupan;
    public String list;

    public String getSearchtype() {
        return searchtype;
    }

    public void setSearchtype(String searchtype) {
        this.searchtype = searchtype;
    }

    public T getLoupan() {
        return loupan;
    }

    public void setLoupan(T loupan) {
        this.loupan = loupan;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "SFWResultBean{" +
                "searchtype='" + searchtype + '\'' +
                ", loupan=" + loupan +
                ", list='" + list + '\'' +
                '}';
    }
}
