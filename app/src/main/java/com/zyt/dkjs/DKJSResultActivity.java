package com.zyt.dkjs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyt.HttpUtil.Bean.MYCHBean;
import com.zyt.R;
import com.zyt.util.ToastUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DKJSResultActivity extends AppCompatActivity {

    @InjectView(R.id.naviTitle)
    TextView naviTitle;
    @InjectView(R.id.dkzeTitle)
    TextView dkzeTitle;
    @InjectView(R.id.dkze)
    TextView dkze;
    @InjectView(R.id.zflx)
    TextView zflx;
    @InjectView(R.id.dkys)
    TextView dkys;
    @InjectView(R.id.myhk)
    TextView myhk;
    @InjectView(R.id.myhkDEBJ)
    TextView myhkDEBJ;
    @InjectView(R.id.debxLayout)
    RelativeLayout debxLayout;
    @InjectView(R.id.debjLayout)
    RelativeLayout debjLayout;

    private boolean isDEBX;
    private ArrayList<MYCHBean> resultList = new ArrayList<MYCHBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debxresult);
        ButterKnife.inject(this);

        initData();
    }

    private void initData() {
        Bundle b = getIntent().getBundleExtra("data");
        DecimalFormat df = new DecimalFormat(",###.##");

        if (!b.getBoolean("isZH")){
            int dkje = b.getInt("dkje");
            int dkqx = b.getInt("dkqx");
            isDEBX = b.getBoolean("isDEBX");

            Log.e("bee","ll:"+b.getString("dkll"));

            double dknll = Double.parseDouble(b.getString("dkll")) / 100;
            double dkyll = dknll / 12;

            if (isDEBX){
                double myyge = getDEBXMyyge(dkje,dkyll,dkqx);
                double zflxD = getDEBXZflx(myyge,dkqx,dkje);

                dkzeTitle.setText(df.format(dkje+zflxD));
                dkze.setText(df.format(dkje+zflxD));
                zflx.setText(df.format(zflxD));
                myhk.setText(df.format(myyge));

                naviTitle.setText("等额本息");
                debxLayout.setVisibility(View.VISIBLE);
                debjLayout.setVisibility(View.GONE);
            }else{
                double zflxD = getDEBJZflx(dkje,dkyll,dkqx);

                zflx.setText(df.format(zflxD));
                dkzeTitle.setText(df.format(dkje+zflxD));
                dkze.setText(df.format(dkje+zflxD));

                caculateMYCH(dkje,dkyll,dkqx);

                naviTitle.setText("等额本金");
                debxLayout.setVisibility(View.GONE);
                debjLayout.setVisibility(View.VISIBLE);
            }
            dkys.setText(Integer.toString(dkqx * 12));
        }else{
            isDEBX = b.getBoolean("isDEBX");

            int dkqx = b.getInt("dkqx");
            double sydkll = Double.parseDouble(b.getString("sydkll")) / 100 /12;
            double gjjdkll = Double.parseDouble(b.getString("gjjdkll")) / 100 /12;

            int sydkje = b.getInt("sydkje");
            int gjjdkje = b.getInt("gjjdkje");


            if (isDEBX){
                double symyyge = getDEBXMyyge(sydkje,sydkll,dkqx);
                double syzflxD = getDEBXZflx(symyyge,dkqx,sydkje);

                double gjjmyyge = getDEBXMyyge(gjjdkje,gjjdkll,dkqx);
                double gjjzflxD = getDEBXZflx(gjjmyyge,dkqx,gjjdkje);

                dkzeTitle.setText(df.format(sydkje+syzflxD+gjjdkje+gjjzflxD));
                dkze.setText(df.format(sydkje+syzflxD+gjjdkje+gjjzflxD));
                zflx.setText(df.format(syzflxD+gjjzflxD));
                myhk.setText(df.format(symyyge+gjjmyyge));

                naviTitle.setText("等额本息");
                debxLayout.setVisibility(View.VISIBLE);
                debjLayout.setVisibility(View.GONE);
            }else{
                double syzflxD = getDEBJZflx(sydkje,sydkll,dkqx);
                double gjjzflxD = getDEBJZflx(gjjdkje,gjjdkll,dkqx);

                zflx.setText(df.format(syzflxD+gjjzflxD));
                dkzeTitle.setText(df.format(sydkje+syzflxD+gjjdkje+gjjzflxD));
                dkze.setText(df.format(sydkje+syzflxD+gjjdkje+gjjzflxD));

                caculateMYCH(sydkje,sydkll,dkqx);
                caculateMYCH(gjjdkje,gjjdkll,dkqx);
                Log.e("bee",resultList.toString());

                naviTitle.setText("等额本金");
                debxLayout.setVisibility(View.GONE);
                debjLayout.setVisibility(View.VISIBLE);
            }

            dkys.setText(Integer.toString(dkqx * 12));
        }



    }

    private double getDEBXMyyge (int dkje,double dkyll,int dkqx){
        return dkje * dkyll * Math.pow(1 + dkyll, dkqx * 12) / (Math.pow(1 + dkyll, dkqx * 12) - 1);
    }

    private double getDEBXZflx (double myyge,int dkqx,int dkje){
        return myyge * dkqx * 12 - dkje;
    }

    private double getDEBJZflx (int dkje,double dkyll,int dkqx){
        return dkje*dkyll*(dkqx*12/2+0.5);
    }

    private void caculateMYCH(double dkbj,double yll,int dkqx){

        double chbj = dkbj / ( dkqx * 12 );
        double alreadyCHBJ = 0;
        for (int i = 0 ; i<dkqx*12 ; i++) {
            if (i == resultList.size()) {
                MYCHBean bean = new MYCHBean();
                bean.chbj = chbj;
                bean.month = i + 1;
                bean.chlx = (dkbj - alreadyCHBJ) * yll;
                bean.sybj = dkbj - alreadyCHBJ;
                resultList.add(bean);
            } else {
                MYCHBean bean = resultList.get(i);
                bean.chbj += chbj;
                bean.chlx += (dkbj - alreadyCHBJ) * yll;
                bean.sybj += dkbj - alreadyCHBJ;
                resultList.add(bean);
            }
            alreadyCHBJ += chbj;
        }
    }

    @OnClick({R.id.backBtn, R.id.myhkDEBJ})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.myhkDEBJ:
                Intent i = new Intent();
                i.setClass(DKJSResultActivity.this,DEBJResultListActivity.class);
                i.putExtra("myhkData",(Serializable) resultList);
                try {
                    startActivity(i);

                }catch (Exception e){
                    ToastUtils.showShort(DKJSResultActivity.this,"贷款年限过长");
                }
                break;
        }
    }
}
