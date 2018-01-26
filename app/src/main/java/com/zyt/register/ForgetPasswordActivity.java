package com.zyt.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.HttpUtil.Bean.RegisterBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

public class ForgetPasswordActivity extends AppCompatActivity {

    @InjectView(R.id.userPhone)
    EditText userPhone;
    @InjectView(R.id.sendAnalyBtn)
    TextView sendAnalyBtn;
    @InjectView(R.id.analyNum)
    EditText analyNum;

    private int analyPassTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.inject(this);

        initSMSSDK();
    }

    private void initSMSSDK(){
        SMSSDK.initSDK(this,(String) this.getResources().getText(R.string.SMSSDK_APPKEY),(String) this.getResources().getText(R.string.SMSSDK_SECREAT));
        EventHandler eh = new EventHandler(){
            @Override
            public void afterEvent(int i, int i1, Object o) {
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Intent intent = new Intent();
                        intent.setClass(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                        intent.putExtra("phoneNum",userPhone.getText().toString());
                        startActivity(intent);
                    }else if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                    }else if (i ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)o).printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }


    @OnClick({R.id.sendAnalyBtn, R.id.nextStepBtn,R.id.backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendAnalyBtn:
                if (userPhone.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"请输入手机号码",Toast.LENGTH_SHORT).show();
                }else{
                    SMSSDK.getVerificationCode("86",userPhone.getText().toString());
                    sendAnalyBtn.setEnabled(false);

                    analyPassTime = 0;

                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendAnalyBtn.setBackgroundColor(Color.parseColor("#8e8e8e"));
                                    sendAnalyBtn.setText(60-analyPassTime+"秒后可重发");
                                    analyPassTime++;
                                    if (analyPassTime == 60){
                                        sendAnalyBtn.setBackgroundColor(Color.parseColor("#d22d26"));
                                        sendAnalyBtn.setText("发送验证码");
                                        sendAnalyBtn.setEnabled(true);
                                        t.cancel();
                                    }
                                }
                            });
                        }
                    },0,1000);
                }
                break;
            case R.id.nextStepBtn:
                if (analyNum.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                }else{
                    SMSSDK.submitVerificationCode("86",userPhone.getText().toString(),analyNum.getText().toString());
                }
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
