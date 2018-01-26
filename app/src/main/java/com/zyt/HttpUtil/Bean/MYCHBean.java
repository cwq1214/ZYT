package com.zyt.HttpUtil.Bean;

import java.io.Serializable;

/**
 * Created by chenbifeng on 16/7/28.
 */
public class MYCHBean implements Serializable {
    public int month;
    public double chlx;
    public double chbj;
    public double sybj;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getChlx() {
        return chlx;
    }

    public void setChlx(double chlx) {
        this.chlx = chlx;
    }

    public double getChbj() {
        return chbj;
    }

    public void setChbj(double chbj) {
        this.chbj = chbj;
    }

    public double getSybj() {
        return sybj;
    }

    public void setSybj(double sybj) {
        this.sybj = sybj;
    }

    @Override
    public String toString() {
        return "MYCHBean{" +
                "month=" + month +
                ", chlx=" + chlx +
                ", chbj=" + chbj +
                ", sybj=" + sybj +
                '}';
    }
}
