package com.zyt.HttpUtil.Bean.BDBean;

/**
 * Created by chenbifeng on 16/7/26.
 */
public class BDHouseBean<T1,T2> {
    public String name;
    public T1 location;
    public String address;
    public String street_id;
    public String telephone;
    public int detail;
    public String uid;
    public T2 detail_info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T1 getLocation() {
        return location;
    }

    public void setLocation(T1 location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public T2 getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(T2 detail_info) {
        this.detail_info = detail_info;
    }

    @Override
    public String toString() {
        return "BDHouseBean{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", address='" + address + '\'' +
                ", street_id='" + street_id + '\'' +
                ", telephone='" + telephone + '\'' +
                ", detail=" + detail +
                ", uid='" + uid + '\'' +
                ", detail_info=" + detail_info +
                '}';
    }
}
