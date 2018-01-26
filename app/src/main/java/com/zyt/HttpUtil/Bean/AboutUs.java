package com.zyt.HttpUtil.Bean;

/**
 * Created by v7 on 2016/8/30.
 */
public class AboutUs {
    //{"forUser": "", "forWorker": "", "version": "1.0", "data": {"iosImg": "http://120.76.233.32/iosImg.jpg", "androidImg": "http://120.76.233.32/androidImg.jpg"}, "ret": "success", "code": 200}
    String iosImg;
    String androidImg;

    public String getIosImg() {
        return iosImg;
    }

    public void setIosImg(String iosImg) {
        this.iosImg = iosImg;
    }

    public String getAndroidImg() {
        return androidImg;
    }

    public void setAndroidImg(String androidImg) {
        this.androidImg = androidImg;
    }
}
