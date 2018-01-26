package com.zyt.HttpUtil.Bean;

import java.util.HashMap;

/**
 * Created by chenbifeng on 16/7/28.
 */
public class LoanTaxBean {
    public HashMap<String,String> business;
    public HashMap<String,String> gjj;

    public HashMap<String, String> getBusiness() {
        return business;
    }

    public void setBusiness(HashMap<String, String> business) {
        this.business = business;
    }

    public HashMap<String, String> getGjj() {
        return gjj;
    }

    public void setGjj(HashMap<String, String> gjj) {
        this.gjj = gjj;
    }

    @Override
    public String toString() {
        return "LoanTaxBean{" +
                "business=" + business +
                ", gjj=" + gjj +
                '}';
    }
}
