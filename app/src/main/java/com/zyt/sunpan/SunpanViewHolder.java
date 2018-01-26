package com.zyt.sunpan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyt.R;
import com.zyt.sunpan.bean.Sunpan;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class SunpanViewHolder extends RecyclerView.ViewHolder {


    @InjectView(R.id.apartmentName)
    TextView apartmentName;
    @InjectView(R.id.price)
    TextView price;
    @InjectView(R.id.dong)
    TextView dong;
    @InjectView(R.id.ceng)
    TextView ceng;
    @InjectView(R.id.area)
    TextView area;
    @InjectView(R.id.contact)
    TextView contact;
    @InjectView(R.id.company)
    TextView company;
    @InjectView(R.id.phonenumber)
    TextView phonenumber;

    Sunpan sunpan;
    @InjectView(R.id.dw)
    RelativeLayout dw;

    public SunpanViewHolder(Context context) {
        this(LayoutInflater.from(context).inflate(R.layout.layout_sunnpan_viewholder, null));
    }

    public SunpanViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void init(Sunpan sunpan) {
        this.sunpan = sunpan;

        apartmentName.setText(sunpan.getSpName());
        price.setText(sunpan.getSpPrice() + " 元/平方米");
        dong.setText(sunpan.getSpBuildingNo());
        ceng.setText(sunpan.getSpFloor());
        if (sunpan.getSpArea().equalsIgnoreCase("")){
            dw.setVisibility(View.GONE);
        }else {
            dw.setVisibility(View.VISIBLE);
        }
        area.setText(sunpan.getSpArea());
        contact.setText(sunpan.getSpUsername());
        company.setText(sunpan.getSpCompanyName());
        phonenumber.setText(sunpan.getSpPhone());

    }
}
