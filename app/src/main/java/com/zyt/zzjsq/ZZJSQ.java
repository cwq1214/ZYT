package com.zyt.zzjsq;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ZZJSQ extends AppCompatActivity {

    @InjectView(R.id.ygzj)
    TextView ygzj;
    @InjectView(R.id.fwmj)
    TextView fwmj;
    @InjectView(R.id.yqsje)
    TextView yqsje;
    @InjectView(R.id.xgszj)
    TextView xgszj;
    @InjectView(R.id.yqssj)
    TextView yqssj;
    @InjectView(R.id.isOnlyOneHouse)
    CheckBox isOnlyOneHouse;
    @InjectView(R.id.buyFirst)
    TextView buyFirst;
    @InjectView(R.id.buySecond)
    TextView buySecond;
    @InjectView(R.id.buyMany)
    TextView buyMany;
    @InjectView(R.id.qs)
    TextView qs;
    @InjectView(R.id.zzs)
    TextView zzs;
    @InjectView(R.id.grsds)
    TextView grsds;
    @InjectView(R.id.hj)
    TextView hj;
    @InjectView(R.id.fjs)
    TextView fjs;

    private int selectYear;
    private int selectMonth;
    private int selectDay;
    private int nowYear;
    private int nowMonth;
    private int nowDay;

    private int buyKind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zzjsq);
        ButterKnife.inject(this);

        Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
        Date mydate=new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);
        selectYear=mycalendar.get(Calendar.YEAR);
        selectMonth=mycalendar.get(Calendar.MONTH);
        selectDay=mycalendar.get(Calendar.DAY_OF_MONTH);

        nowYear=mycalendar.get(Calendar.YEAR);
        nowMonth=mycalendar.get(Calendar.MONTH);
        nowDay=mycalendar.get(Calendar.DAY_OF_MONTH);
    }

    @OnClick({R.id.backBtn, R.id.yqssj, R.id.buyFirst, R.id.buySecond, R.id.buyMany, R.id.caculateBtn, R.id.resetBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.yqssj:
                DatePickerDialog dd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectYear=i;
                        selectMonth=i1;
                        selectDay=i2;
                        yqssj.setText(selectYear+"."+(selectMonth+1)+"."+selectDay);
                    }
                },selectYear,selectMonth,selectDay);
                dd.show();
                break;
            case R.id.buyFirst:
                buyKind = 0;
                buyFirst.setBackgroundColor(Color.parseColor("#dddddd"));
                buySecond.setBackgroundColor(Color.parseColor("#f1f1f1"));
                buyMany.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.buySecond:
                buyKind = 1;
                buyFirst.setBackgroundColor(Color.parseColor("#f1f1f1"));
                buySecond.setBackgroundColor(Color.parseColor("#dddddd"));
                buyMany.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.buyMany:
                buyKind = 2;
                buyFirst.setBackgroundColor(Color.parseColor("#f1f1f1"));
                buySecond.setBackgroundColor(Color.parseColor("#f1f1f1"));
                buyMany.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
            case R.id.caculateBtn:
                if (ygzj.getText().length() == 0 || fwmj.getText().length() == 0 || yqsje.getText().length() == 0 || xgszj.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"请填写完整信息",Toast.LENGTH_SHORT).show();
                }else {
                    if (!isNumeric(ygzj.getText().toString()) || !isNumeric(fwmj.getText().toString()) || !isNumeric(yqsje.getText().toString()) || !isNumeric(xgszj.getText().toString())){
                        Toast.makeText(getApplicationContext(),"信息格式错误",Toast.LENGTH_SHORT).show();
                    }else {
                        caculateData();
                    }
                }
                break;
            case R.id.resetBtn:
                ygzj.setText("");
                fwmj.setText("");
                yqsje.setText("");
                xgszj.setText("");
                yqssj.setText("");

                selectYear = nowYear;
                selectMonth = nowMonth;
                selectDay = nowDay;

                isOnlyOneHouse.setChecked(false);

                buyKind = 0;
                buyFirst.setBackgroundColor(Color.parseColor("#dddddd"));
                buySecond.setBackgroundColor(Color.parseColor("#f1f1f1"));
                buyMany.setBackgroundColor(Color.parseColor("#f1f1f1"));

                qs.setText("");
                zzs.setText("");
                grsds.setText("");
                hj.setText("");
                break;
        }
    }

    private void caculateData(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long deltaDay = 0;
        try {
            Date nowDate= sdf.parse(nowYear+"-"+nowMonth+"-"+nowDay+" 00:00:00");
            Date selectDate= sdf.parse(selectYear+"-"+selectMonth+"-"+selectDay+" 00:00:00");
            deltaDay = (nowDate.getTime() - selectDate.getTime())/(24*60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DecimalFormat df = new DecimalFormat("0.00");

        double qdD;
        double zzsD;
        if (deltaDay<=730){
            if (buyKind == 0){
                if (Float.parseFloat(fwmj.getText().toString()) <= 90){
                    qdD = Float.parseFloat(xgszj.getText().toString())/1.05*0.01;
                }else{
                    qdD = Float.parseFloat(xgszj.getText().toString())/1.05*0.015;
                }
            }else if (buyKind == 1){
                if (Float.parseFloat(fwmj.getText().toString()) <= 90){
                    qdD = Float.parseFloat(xgszj.getText().toString())/1.05*0.01;
                }else{
                    qdD = Float.parseFloat(xgszj.getText().toString())/1.05*0.02;
                }
            }else{
                qdD = Float.parseFloat(xgszj.getText().toString())/1.05*0.03;
            }

            zzsD = Float.parseFloat(xgszj.getText().toString())/1.05*0.05;
        }else{
            if (buyKind == 0){
                if (Float.parseFloat(fwmj.getText().toString()) <= 90){
                    qdD = Float.parseFloat(xgszj.getText().toString())*0.01;
                }else{
                    qdD = Float.parseFloat(xgszj.getText().toString())*0.015;
                }
            }else if (buyKind == 1){
                if (Float.parseFloat(fwmj.getText().toString()) <= 90){
                    qdD = Float.parseFloat(xgszj.getText().toString())*0.01;
                }else{
                    qdD = Float.parseFloat(xgszj.getText().toString())*0.02;
                }
            }else{
                qdD = Float.parseFloat(xgszj.getText().toString())*0.03;
            }

            zzsD = Float.parseFloat(xgszj.getText().toString())/1.05*0;
        }

        double grsdsD = 0;
        if (deltaDay>1825){//more than 1825 and only one house
            if (isOnlyOneHouse.isChecked())
                grsdsD = 0;
            else
                grsdsD = (Float.parseFloat(xgszj.getText().toString())-Float.parseFloat(ygzj.getText().toString())-Float.parseFloat(yqsje.getText().toString()))*0.2;
        }else if (deltaDay<=1825 && deltaDay>730){//less than 1825 more than 730
            grsdsD = (Float.parseFloat(xgszj.getText().toString())-Float.parseFloat(ygzj.getText().toString())-Float.parseFloat(yqsje.getText().toString()))*0.2;
        }else if (deltaDay<=730){//less than 730
            grsdsD = (Float.parseFloat(xgszj.getText().toString())/1.05-Float.parseFloat(ygzj.getText().toString())-Float.parseFloat(yqsje.getText().toString()))*0.2;
        }

        qdD=qdD<=0?0:qdD;
        zzsD=zzsD<=0?0:zzsD;
        grsdsD=grsdsD<=0?0:grsdsD;

        double fjsD;
        fjsD = zzsD*0.12;

        qs.setText(df.format(qdD));
        zzs.setText(df.format(zzsD));
        grsds.setText(df.format(grsdsD));
        hj.setText(df.format(qdD+zzsD+grsdsD));
        fjs.setText(df.format(fjsD));
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
