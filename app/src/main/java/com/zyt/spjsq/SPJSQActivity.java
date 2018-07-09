package com.zyt.spjsq;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zyt.R;
import com.zyt.util.ToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SPJSQActivity extends AppCompatActivity {

    @InjectView(R.id.ygzj)
    EditText ygzj;
    @InjectView(R.id.yqsje)
    EditText yqsje;
    @InjectView(R.id.xgszj)
    EditText xgszj;
    @InjectView(R.id.caculateBtn)
    Button caculateBtn;
    @InjectView(R.id.resetBtn)
    Button resetBtn;
    @InjectView(R.id.yqssj)
    TextView yqssj;
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


    private float ygzj_f;
    private float yqsje_f;
    private float xgszj_f;
    private int year;
    private int month;
    private int day;
    private int selectYear;
    private int selectMonth;
    private int selectDay;

    private int nowYear;
    private int nowMonth;
    private int nowDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spjsq);
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

    @OnClick(R.id.share)
    public void onShareClick(){
        if (TextUtils.isEmpty(hj.getText().toString())){
            ToastUtils.showShort(this,"请先计算");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("ygj="+ygzj.getText().toString());
        sb.append("&yqs="+yqsje.getText().toString());
        sb.append("&xgs="+xgszj.getText().toString());
        sb.append("&yqst="+yqssj.getText().toString());
        sb.append("&qs="+qs.getText().toString());
        sb.append("&zzs="+zzs.getText().toString());
        sb.append("&zze="+zze.getText().toString());
        sb.append("&kc="+kcje.getText().toString());
        sb.append("&td="+tdzzs.getText().toString());
        sb.append("&gr="+grsds.getText().toString());
        sb.append("&yh="+yhs.getText().toString());
        sb.append("&hj="+hj.getText().toString());


        String title ="房秘书";
        String content = "商铺计税器计算结果";
        String url = getString(R.string.baseUrl)+"/server/web/shangpu?"+sb.toString();

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this,R.drawable.logo3));  //缩略图
        web.setDescription(content);//描述
        new ShareAction(this).withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE).open();

    }


    @OnClick({R.id.caculateBtn, R.id.resetBtn, R.id.yqssj, R.id.backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.caculateBtn:
                analyInputMsg();
                break;
            case R.id.resetBtn:
                ygzj.setText("");
                yqsje.setText("");
                xgszj.setText("");
                yqssj.setText("");

                qs.setText("");
                zzs.setText("");
                zze.setText("");
                zzl.setText("");
                kcje.setText("");
                tdzzs.setText("");
                grsds.setText("");
                yhs.setText("");
                hj.setText("");
                break;
            case R.id.yqssj:
                DatePickerDialog dp = new DatePickerDialog(SPJSQActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectYear=i;
                        selectMonth=i1;
                        selectDay=i2;
                        yqssj.setText(selectYear+"."+(selectMonth+1)+"."+selectDay);
                    }
                },selectYear,selectMonth,selectDay);
                dp.show();
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }


    private void analyInputMsg() {
        if (ygzj.getText().length() == 0 || yqsje.getText().length() == 0 || xgszj.getText().length() == 0 || yqssj.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "请填写完整", Toast.LENGTH_SHORT).show();
        } else {
//            if (!isNumeric(ygzj.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "请填写正确的原购总价", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!isNumeric(yqsje.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "请填写正确的原契税金额", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (!isNumeric(xgszj.getText().toString())) {
//                Toast.makeText(getApplicationContext(), "请填写正确的新过税总价", Toast.LENGTH_SHORT).show();
//                return;
//            }

            ygzj_f = Float.parseFloat(ygzj.getText().toString());
            yqsje_f = Float.parseFloat(yqsje.getText().toString());
            xgszj_f = Float.parseFloat(xgszj.getText().toString());
            year = selectYear;
            month = selectMonth;
            day = selectDay;
            showData();
//
//            Bundle b = new Bundle();
//            b.putFloat("ygzj",Float.parseFloat(ygzj.getText().toString()));
//            b.putFloat("yqsje",Float.parseFloat(yqsje.getText().toString()));
//            b.putFloat("xgszj",Float.parseFloat(xgszj.getText().toString()));
//            b.putInt("year",selectYear);
//            b.putInt("month",selectMonth);
//            b.putInt("day",selectDay);
//            b.putInt("nowYear",nowYear);
//            b.putInt("nowMonth",nowMonth);
//            b.putInt("nowDay",nowDay);
//
//            Intent i = new Intent();
//            i.setClass(SPJSQActivity.this,SPJSQResultActivity.class);
//            i.putExtra("data",b);
//            startActivity(i);
        }
    }
    private void showData(){

        double ygzjD = 0;
        double yqsjeD = 0;
        double xgszjD = 0;
        Date yqssj ;


        double fjsD = 0;
        double qsD = 0;
        double zzsD = 0;
        double zzlD= 0;
        double zzeD = 0;
        double kcjeD = 0;
        double tdzzsD = 0;
        double grsdsD = 0;
        double yhsD = 0;
        double hjD_sell = 0;
        double hjD_buy = 0;

        ygzjD = ygzj_f;
        yqsjeD = yqsje_f;
        xgszjD = xgszj_f;
        int difYear = 0;
        int difMonth = 0;
        int term=0;//上手至今年限
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            yqssj = sdf.parse(year+"-"+month+"-"+day);
            Date now = new Date();

            difYear = now.getYear() - yqssj.getYear();
            difMonth = now.getMonth() - yqssj.getMonth();
            term = (int) (difYear + difMonth*0.1 +0.5);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        zzsD = (xgszjD-ygzjD) * 0.056;
        fjsD = zzsD * 0.12;
        yhsD = xgszjD * 0.0005;

        kcjeD = ygzjD * (1+0.05*term) + fjsD + yhsD + yqsjeD;
        zzeD = xgszjD - kcjeD ;
        zzlD = new BigDecimal(zzeD / kcjeD *100 ).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        if (zzlD<=50){
            tdzzsD = zzeD * 0.3;
        }else if (zzlD<=100){
            tdzzsD = zzeD * 0.4 - kcjeD * 0.05;
        }else if (zzlD <=200){
            tdzzsD = zzeD *0.5 - kcjeD *0.15;
        }else {
            //zzlD>200
            tdzzsD = zzeD*0.6 - kcjeD * 0.35;
        }

        grsdsD = (xgszjD - ygzjD - yhsD - fjsD - tdzzsD)*0.2;

        hjD_sell = zzsD + fjsD + yhsD + tdzzsD + grsdsD;

        qsD = xgszjD * 0.03;
        hjD_buy = yhsD + qsD;


        qsD = qsD<0?0:qsD;
        zzsD = zzsD<0?0:zzsD;
        zzlD = zzlD<0?0:zzlD;
        zzeD = zzeD<0?0:zzeD;
        kcjeD = kcjeD<0?0:kcjeD;
        tdzzsD = tdzzsD<0?0:tdzzsD;


        grsdsD = grsdsD<0?0:grsdsD;
        yhsD = yhsD<0?0:yhsD;

        DecimalFormat df = new DecimalFormat("0.00");

        qs.setText(df.format(qsD));
        zzs.setText(df.format(zzsD));
        zze.setText(df.format(zzeD));
        zzl.setText(df.format(zzlD));
        kcje.setText(df.format(kcjeD));
        tdzzs.setText(df.format(tdzzsD));
        grsds.setText(df.format(grsdsD));
        yhs.setText(df.format(yhsD));
        hj.setText(df.format(hjD_buy+hjD_sell));
    }

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private boolean isDateFormat(String str) {
        Pattern pattern = Pattern.compile("[0-9]{4}.[0-9]{2}.[0-9]{2}");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
