package com.zyt.HttpUtil.Bean.SFWBean;

/**
 * Created by chenbifeng on 16/7/27.
 */
public class SFWHouseBean {
    public String newcode;
    public String projname;
    public double x;
    public double y;
    public String price;
    public String domain;
    public String address;
    public String tao;

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getProjname() {
        return projname;
    }

    public void setProjname(String projname) {
        this.projname = projname;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTao() {
        return tao;
    }

    public void setTao(String tao) {
        this.tao = tao;
    }

    @Override
    public String toString() {
        return "SFWHouseBean{" +
                "newcode='" + newcode + '\'' +
                ", projname='" + projname + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", price='" + price + '\'' +
                ", domain='" + domain + '\'' +
                ", address='" + address + '\'' +
                ", tao='" + tao + '\'' +
                '}';
    }
}
