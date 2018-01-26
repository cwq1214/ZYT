package com.zyt.HttpUtil.Bean.BDBean;

/**
 * Created by chenbifeng on 16/7/26.
 */
public class BDHouseInfoBean {
    public String tag;
    public String type;
    public String detail_url;
    public String price;
    public String overall_rating;
    public String image_num;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(String overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getImage_num() {
        return image_num;
    }

    public void setImage_num(String image_num) {
        this.image_num = image_num;
    }

    @Override
    public String toString() {
        return "BDHouseInfoBean{" +
                "tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", detail_url='" + detail_url + '\'' +
                ", price='" + price + '\'' +
                ", overall_rating='" + overall_rating + '\'' +
                ", image_num='" + image_num + '\'' +
                '}';
    }
}
