package com.zyt.HttpUtil.Bean.BDBean;

/**
 * Created by chenbifeng on 16/7/26.
 */
public class BDLocationBean {
    public double lat;
    public double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "BDLocationBean{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
