package com.zyt.WYSL;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.util.ConstList;
import com.zyt.util.ToastUtils;
import com.zyt.util.Util;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class WYSLActivity extends AppCompatActivity {

    @InjectView(R.id.lpmc)
    EditText lpmc;
    @InjectView(R.id.fwmj)
    EditText fwmj;
    @InjectView(R.id.slje)
    EditText slje;
    @InjectView(R.id.lxr)
    EditText lxr;
    @InjectView(R.id.lxdh)
    EditText lxdh;
    @InjectView(R.id.dkjeT)
    TextView dkjeT;
    @InjectView(R.id.lxrT)
    TextView lxrT;
    @InjectView(R.id.submitBtn)
    TextView submit;

    boolean isSubmit =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wysl);
        ButterKnife.inject(this);

        lxdh.setText(Util.getUserPhone(WYSLActivity.this));

        dkjeT.setText(Html.fromHtml("<font color='red'>*</font>贷款金额"));
        lxrT.setText(Html.fromHtml("<font color='red'>*</font>联系人"));
    }


    @OnClick({R.id.backBtn, R.id.submitBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.submitBtn:
                if (isSubmit){
                    finish();
                    return;
                }
                if (slje.getText().length() == 0 || lxr.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    if (Util.getUserID(WYSLActivity.this).equalsIgnoreCase("")) {

                    } else {
                        OkHttpUtils.post().url(getString(R.string.baseUrl) + "/server/shulou/add")
                                .addParams("slName", lpmc.getText().toString())
                                .addParams("slPrice", slje.getText().toString())
                                .addParams("slArea", fwmj.getText().toString())
                                .addParams("slUsername", lxr.getText().toString())
                                .addParams("slPhone", lxdh.getText().toString())
                                .addParams("userId", Util.getUserID(WYSLActivity.this))
                                .build()
                                .execute(new BeanCallBack<ServerBean<Object>>() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.e("bee", "error");
                                    }

                                    @Override
                                    public void onResponse(ServerBean<Object> response, int id) {
                                        Log.e("bee", response.toString());
                                        if (response.getRet().equalsIgnoreCase("success")) {
                                            Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                                            submit.setText("完成");
                                            isSubmit = true;
                                        }else {
                                            ToastUtils.show(WYSLActivity.this,response.getForUser(),Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                    }
                }
                break;
        }
    }

    private String getSmsMsgContent() {
        return "楼盘名称:" + lpmc.getText().toString() + "\n" +
                "房屋面积:" + fwmj.getText().toString() + "\n" +
                "赎楼金额:" + slje.getText().toString() + "\n" +
                "联系人:" + lxr.getText().toString() + "\n" +
                "联系电话:" + lxdh.getText().toString();
    }
}
