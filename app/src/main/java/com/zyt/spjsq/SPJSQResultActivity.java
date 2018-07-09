package com.zyt.spjsq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.zyt.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

@Deprecated
public class SPJSQResultActivity extends AppCompatActivity {

    @InjectView(R.id.qs)
    TextView qs;
    @InjectView(R.id.zzs)
    TextView zzs;
    @InjectView(R.id.zzl)
    TextView zzl;
    @InjectView(R.id.zze)
    TextView zze;
    @InjectView(R.id.kcje)
    TextView kcje;
    @InjectView(R.id.tdzzs)
    TextView tdzzs;
    @InjectView(R.id.grsds)
    TextView grsds;
    @InjectView(R.id.yhs)
    TextView yhs;
    @InjectView(R.id.hj)
    TextView hj;

    private float ygzj;
    private float yqsje;
    private float xgszj;
    private int nowYear;
    private int nowMonth;
    private int nowDay;
    private int year;
    private int month;
    private int day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spjsqresult);
        ButterKnife.inject(this);

        getBaseData();
        showData();
    }

    private void getBaseData(){
        Bundle b = getIntent().getBundleExtra("data");
        ygzj = b.getFloat("ygzj");
        yqsje = b.getFloat("yqsje");
        xgszj = b.getFloat("xgszj");
        nowYear = b.getInt("nowYear");
        nowMonth = b.getInt("nowMonth");
        nowDay = b.getInt("nowDay");
        year = b.getInt("year");
        month = b.getInt("month");
        day = b.getInt("day");

        Log.e("bee",toString());
    }

    private void showData(){
        double qsD = xgszj/1.05*0.03;
        double zzsD = (xgszj/1.05-ygzj)*0.05;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int detailTime = 0;
        try {
            Date selectDate = sdf.parse(year+"-"+month+"-"+day);
            Date nowDate = sdf.parse(nowYear+"-"+nowMonth+"-"+nowDay);
            detailTime = (int)(((double)nowDate.getTime()-(double)selectDate.getTime())/1000/3600/24/365+0.5);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        int detailTime = (int)((nowYear+nowMonth/12) - (year+month/12) + 0.5);
        Log.e("bee","detailTIme:"+detailTime+" "+yqsje+" "+ygzj);
        double kcjeD = ygzj*detailTime*0.05+yqsje+ygzj;
        double zzeD = xgszj/1.05-kcjeD;
        double zzlD = zzeD/kcjeD*100;
        double tdzzsD;
        if (zzlD<50){
            tdzzsD = zzeD*0.3;
        }else if (zzlD<100){
            tdzzsD = zzeD*0.4-kcjeD*0.05;
        }else if (zzlD<200){
            tdzzsD = zzeD*0.5-kcjeD*0.15;
        }else {
            tdzzsD = zzeD*0.6-kcjeD*0.35;
        }

        qsD = qsD<0?0:qsD;
        zzsD = zzsD<0?0:zzsD;
        zzlD = zzlD<0?0:zzlD;
        zzeD = zzeD<0?0:zzeD;
        kcjeD = kcjeD<0?0:kcjeD;
        tdzzsD = tdzzsD<0?0:tdzzsD;

        double grsdsD = (xgszj/1.05-ygzj-yqsje-tdzzsD)*0.2;
        double yhsD = xgszj/1.05*0.0005*2;

        grsdsD = grsdsD<0?0:grsdsD;
        yhsD = yhsD<0?0:yhsD;

        DecimalFormat df = new DecimalFormat("0.00");

        qs.setText(df.format(qsD));
        zzs.setText(df.format(zzsD));
        zze.setText(df.format(zzeD));
        zzl.setText(df.format(zzlD)+"%");
        kcje.setText(df.format(kcjeD));
        tdzzs.setText(df.format(tdzzsD));
        grsds.setText(df.format(grsdsD));
        yhs.setText(df.format(yhsD));
        hj.setText(df.format(qsD+zzsD+tdzzsD+grsdsD+yhsD));
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        finish();
    }

    @Override
    public String toString() {
        return "SPJSQResultActivity{" +
                "ygzj=" + ygzj +
                ", yqsje=" + yqsje +
                ", xgszj=" + xgszj +
                ", nowYear=" + nowYear +
                ", nowMonth=" + nowMonth +
                ", year=" + year +
                ", month=" + month +
                '}';
    }
}
