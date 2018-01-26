package com.zyt.sunpan;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zyt.sunpan.bean.Sunpan;

import java.util.ArrayList;

/**
 * Created by v7 on 2016/8/24.
 */
public class MySunpanAdapter extends RecyclerView.Adapter {

    ArrayList<Sunpan> sunpan;

    MySunpanViewHolder.OnDelClickListener onDelClickListener;

    public void setOnDelClickListener(MySunpanViewHolder.OnDelClickListener onDelClickListener) {
        this.onDelClickListener = onDelClickListener;
    }

    public ArrayList<Sunpan> getSunpan() {
        return sunpan;
    }

    public void setSunpan(ArrayList<Sunpan> sunpan) {
        this.sunpan = sunpan;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySunpanViewHolder(parent.getContext(),onDelClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SunpanViewHolder) holder).init(sunpan.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (sunpan==null)
            return 0;
        return sunpan.size();
    }
}
