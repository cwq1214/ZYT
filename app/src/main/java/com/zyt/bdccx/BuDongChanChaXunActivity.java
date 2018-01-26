package com.zyt.bdccx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zyt.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/8/30.
 */
public class BuDongChanChaXunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bdccx);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.fcjyyy)
    public void onFcjyyyClick(){
        BrowseActivity.launch(this,"http://218.13.180.149/oa/swap/NetWork.do?method=forwordNerWorkNewSheet");

    }

    @OnClick(R.id.djxxcx)
    public void onDjxxcxClick(){
        BrowseActivity.launch(this,"http://61.143.53.130:8085/zhpubweb/equityserch.aspx");
    }
    @OnClick(R.id.bljdcx)
    public void onBljdcxClick(){
        BrowseActivity.launch(this,"http://61.143.53.130:8085/zhpubweb/progressSerch.aspx");
    }
    @OnClick(R.id.htbags)
    public void onHtbagsClick(){
        BrowseActivity.launch(this,"http://61.143.53.130:8086/zhpubweb/projectQuery.aspx");
    }
    @OnClick(R.id.spftba)
    public void onSpftbaClick(){
        BrowseActivity.launch(this,"https://61.143.53.130:4431/zhsaleweb/Default.aspx");
    }
    @OnClick(R.id.esfwsyy)
    public void onEsfwsyy(){
        BrowseActivity.launch(this,"http://61.143.53.130:8088/zhStockWeb/PersonLogin.aspx");
    }
    @OnClick(R.id.jrbmwssl)
    public void onJrbmwsslClick(){
        BrowseActivity.launch(this,"https://61.143.53.130:4431/zhbankweb/Default.aspx");
    }
    @OnClick(R.id.zwbmcxdl)
    public void onZwbmcxdlClick(){
        BrowseActivity.launch(this,"http://61.143.53.130:8085/zhcxweb/Default.aspx");
    }

    @OnClick(R.id.backBtn)
    public void onBackBtnClick(){
        finish();
    }

}
