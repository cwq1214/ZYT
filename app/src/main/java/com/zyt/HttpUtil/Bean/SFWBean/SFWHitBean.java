package com.zyt.HttpUtil.Bean.SFWBean;

import java.util.List;

/**
 * Created by chenbifeng on 16/7/27.
 */
public class SFWHitBean<T> {
    public List<T> hit;

    public List<T> getHit() {
        return hit;
    }

    public void setHit(List<T> hit) {
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "SFWHitBean{" +
                "hit=" + hit +
                '}';
    }
}
