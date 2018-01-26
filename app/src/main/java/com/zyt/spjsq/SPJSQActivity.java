package com.zyt.spjsq;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.R;

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
            if (!isNumeric(ygzj.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请填写正确的原购总价", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isNumeric(yqsje.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请填写正确的原契税金额", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isNumeric(xgszj.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请填写正确的新过税总价", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle b = new Bundle();
            b.putFloat("ygzj",Float.parseFloat(ygzj.getText().toString()));
            b.putFloat("yqsje",Float.parseFloat(yqsje.getText().toString()));
            b.putFloat("xgszj",Float.parseFloat(xgszj.getText().toString()));
            b.putInt("year",selectYear);
            b.putInt("month",selectMonth);
            b.putInt("day",selectDay);
            b.putInt("nowYear",nowYear);
            b.putInt("nowMonth",nowMonth);
            b.putInt("nowDay",nowDay);

            Intent i = new Intent();
            i.setClass(SPJSQActivity.this,SPJSQResult.class);
            i.putExtra("data",b);
            startActivity(i);
        }
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
