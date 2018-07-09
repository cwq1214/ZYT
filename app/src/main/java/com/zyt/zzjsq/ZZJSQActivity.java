package com.zyt.zzjsq;

import android.app.DatePickerDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zyt.R;
import com.zyt.base.TabEntity;
import com.zyt.util.ToastUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ZZJSQActivity extends AppCompatActivity {

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
    @InjectView(R.id.tl_1)
    SegmentTabLayout tl1;



    private int selectYear;
    private int selectMonth;
    private int selectDay;
    private int nowYear;
    private int nowMonth;
    private int nowDay;

    private int buyKind = 0;
    private String[] mTitles = {"差额20%", "全额1%", "全额1.5%", "全额2%","全额3%"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zzjsq);
        ButterKnife.inject(this);

        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);
        selectYear = mycalendar.get(Calendar.YEAR);
        selectMonth = mycalendar.get(Calendar.MONTH)+1;
        selectDay = mycalendar.get(Calendar.DAY_OF_MONTH);

        nowYear = mycalendar.get(Calendar.YEAR);
        nowMonth = mycalendar.get(Calendar.MONTH)+1;
        nowDay = mycalendar.get(Calendar.DAY_OF_MONTH);


        List tabs = new ArrayList();
        for (int i=0;i<mTitles.length;i++){
            tabs.add(new TabEntity(mTitles[i],0,0));
        }
        tl1.setTabData(mTitles);
    }
    @OnClick(R.id.share)
    public void onShareClick(){


        if (TextUtils.isEmpty(hj.getText())){
            ToastUtils.showShort(this,"请先计算");
            return;
        }





        StringBuilder sb = new StringBuilder();
        sb.append("ygj="+ygzj.getText().toString());
        sb.append("&area="+fwmj.getText().toString());
        sb.append("&yqs="+yqsje.getText().toString());
        sb.append("&xcj="+xgszj.getText().toString());
        sb.append("&yqst="+yqssj.getText().toString());
        sb.append("&unique="+(isOnlyOneHouse.isChecked()?"1":"0"));
        sb.append("&qs="+qs.getText().toString());
        sb.append("&zzs="+zzs.getText().toString());
        sb.append("&fjs="+fjs.getText().toString());
        sb.append("&gr="+grsds.getText().toString());
        sb.append("&hj="+hj.getText().toString());

        sb.append("&way="+(tl1.getCurrentTab()+1));
//        String house = "" ;
//        switch (buyKind){
//            case 0:
//                house=buyFirst.getText().toString();
//                break;
//            case 1:
//                house = buySecond.getText().toString();
//                break;
//            case 2:
//                house = buyMany.getText().toString();
//                break;
//        }
        sb.append("&hourse="+(buyKind+1));

        String title ="房秘书";
        String content = "住宅计税器计算结果";
        String url = getString(R.string.baseUrl)+"/server/web/zhuzhai?"+sb.toString();



        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this,R.drawable.logo3));  //缩略图
        web.setDescription(content);//描述
        new ShareAction(this).withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE).open();

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
                        selectYear = i;
                        selectMonth = i1;
                        selectDay = i2;
                        yqssj.setText(selectYear + "." + (selectMonth + 1) + "." + selectDay);
                    }
                }, selectYear, selectMonth, selectDay);
                dd.show();
                break;
            case R.id.buyFirst:
                buyKind = 0;

                buyFirst.setTextColor(Color.WHITE);
                buyFirst.setBackgroundResource(R.drawable.btn_radio_sel);
                buySecond.setTextColor(Color.parseColor("#70000000"));
                buySecond.setBackgroundResource(0);
                buyMany.setTextColor(Color.parseColor("#70000000"));
                buyMany.setBackgroundResource(0);

//                buyFirst.setBackgroundColor(Color.parseColor("#dddddd"));
//                buySecond.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                buyMany.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.buySecond:
                buyKind = 1;


                buySecond.setTextColor(Color.WHITE);
                buySecond.setBackgroundResource(R.drawable.btn_radio_sel);
                buyFirst.setTextColor(Color.parseColor("#70000000"));
                buyFirst.setBackgroundResource(0);
                buyMany.setTextColor(Color.parseColor("#70000000"));
                buyMany.setBackgroundResource(0);
//
//                buyFirst.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                buySecond.setBackgroundColor(Color.parseColor("#dddddd"));
//                buyMany.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.buyMany:
                buyKind = 2;

                buyMany.setTextColor(Color.WHITE);
                buyMany.setBackgroundResource(R.drawable.btn_radio_sel);
                buyFirst.setTextColor(Color.parseColor("#70000000"));
                buyFirst.setBackgroundResource(0);
                buySecond.setTextColor(Color.parseColor("#70000000"));
                buySecond.setBackgroundResource(0);
//
//                buyFirst.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                buySecond.setBackgroundColor(Color.parseColor("#f1f1f1"));
//                buyMany.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
            case R.id.caculateBtn:
                if (ygzj.getText().length() == 0 || fwmj.getText().length() == 0 || yqsje.getText().length() == 0 || xgszj.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
//                    if (!isNumeric(ygzj.getText().toString()) || !isNumeric(fwmj.getText().toString()) || !isNumeric(yqsje.getText().toString()) || !isNumeric(xgszj.getText().toString())) {
//                        Toast.makeText(getApplicationContext(), "信息格式错误", Toast.LENGTH_SHORT).show();
//                    } else {


                        caculateData();

                        InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//                    }
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

    private void caculateData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("0.00");

        long difYear = 0;//今日年份 - 原契税日期年份
        long difMonth = 0;//今日月份 - 原契税日期月份


        double ygzj = Double.valueOf(this.ygzj.getText().toString());//原购总价
        double xgszj = Double.valueOf(this.xgszj.getText().toString());//新过税总价
        double yqsje = Double.valueOf(this.yqsje.getText().toString());//原契税金额
        double fwmj = Double.valueOf(this.fwmj.getText().toString());//房屋面积

        boolean isUnique=isOnlyOneHouse.isChecked();//唯一住房
        double qsD;//契税
        double zzsD;//增值税
        double grsdsD = 0;//个人所得税

        int grsdsType = tl1.getCurrentTab();
        int buyType = buyKind;
        double buyType_percent = 0;
        double grsds_percent = 0;//征收方式
        double fjsD;//附加税
        double hjD_buy;//买合计
        double hjD_sell;//麦合计


        try {
            Date nowDate = sdf.parse(nowYear + "-" + nowMonth + "-" + nowDay + " 00:00:00");
            Date selectDate = sdf.parse(selectYear + "-" + (selectMonth+1) + "-" + selectDay + " 00:00:00");
            difMonth = nowDate.getMonth() - selectDate.getMonth() ;
            difYear = nowDate.getYear() - selectDate.getYear() ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (difYear + difMonth*0.1 >=2){ //免增值税税
            zzsD = 0;
        }else {
            zzsD = xgszj * 0.05;
        }
        fjsD = zzsD * 0.12;

        if (difYear + difMonth * 0.1 >=5  && isUnique){
            grsdsD = 0;
        }else {
            if (grsdsType == 0){//（新成交总价-原购总价-原契税-附加税）×20%
                grsdsD = ( xgszj - ygzj - yqsje - fjsD ) * 0.2;
            }else {
                switch (grsdsType){
                    case 1:
                        grsds_percent = 0.01;
                        break;
                    case 2:
                        grsds_percent = 0.015;
                        break;
                    case 3:
                        grsds_percent = 0.02;
                        break;
                    case 4:
                        grsds_percent = 0.03;
                        break;
                }
                grsdsD = grsds_percent * xgszj;
            }
        }


        hjD_sell = zzsD + fjsD + grsdsD;

        if (buyType==0){
            if (fwmj>=90){
                buyType_percent = 0.015;
            }else {
                buyType_percent = 0.01;
            }
        }else if (buyType == 1){
            buyType_percent = 0.02;
        }else if (buyType == 2){
            buyType_percent = 0.03;
        }
        hjD_buy = xgszj * buyType_percent;


        hjD_buy = hjD_buy > 0?hjD_buy:0;
        zzsD = zzsD>0?zzsD:0;
        grsdsD = grsdsD >0 ? grsdsD:0;
        hjD_sell = hjD_sell>0?hjD_sell:0;
        fjsD = fjsD>0?fjsD:0;

        qs.setText(df.format(hjD_buy));
        zzs.setText(df.format(zzsD));
        grsds.setText(df.format(grsdsD));
        hj.setText(df.format(hjD_buy + hjD_sell));
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
