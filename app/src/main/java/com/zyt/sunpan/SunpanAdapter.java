package com.zyt.sunpan;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zyt.sunpan.bean.Sunpan;

import java.util.ArrayList;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class SunpanAdapter extends RecyclerView.Adapter{

    ArrayList<Sunpan> sunpan;


    public ArrayList<Sunpan> getSunpan() {
        return sunpan;
    }

    public void setSunpan(ArrayList<Sunpan> sunpan) {
        this.sunpan = sunpan;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SunpanViewHolder(parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SunpanViewHolder) holder).init(sunpan.get(position));
    }

    @Override
    public int getItemCount() {
        if (sunpan==null)
            return 0;
        return sunpan.size();
    }
}
