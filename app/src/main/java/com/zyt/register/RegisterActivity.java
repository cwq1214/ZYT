package com.zyt.register;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.RegisterBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.util.ConstList;
import com.zyt.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();

    @InjectView(R.id.userPhone)
    EditText userPhone;
    @InjectView(R.id.sendAnalyBtn)
    TextView sendAnalyBtn;
    @InjectView(R.id.anlayNum)
    EditText anlayNum;
    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.company)
    EditText company;
    @InjectView(R.id.registerBtn)
    Button registerBtn;

    private int analyPassTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        initSMSSDK();
    }

    private void initSMSSDK(){
        SMSSDK.initSDK(this,(String) this.getResources().getText(R.string.SMSSDK_APPKEY),(String) this.getResources().getText(R.string.SMSSDK_SECREAT));
        EventHandler eh = new EventHandler(){
            @Override
            public void afterEvent(int i, int i1, final Object o) {
                if (i1 == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Log.e(TAG,"提交验证码成功");
                        register();
                    }else if (i == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        Log.e(TAG,"获取验证码成功");
//                        Toast.makeText(getApplicationContext(),"验证码已发送",Toast.LENGTH_SHORT).show();
                    }else if (i ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)o).printStackTrace();
                    if (o instanceof Throwable){
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(((Throwable) o).getMessage());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                if (status > 0 && !TextUtils.isEmpty(des)) {
                                    Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
    }


    @OnClick(R.id.backBtn)
    public void onBackBtnClick(){
        finish();
    }
    @OnClick(R.id.sendAnalyBtn)
    public void onSendAnalyzeBtnClick(){
        if (userPhone.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"请输入手机号码",Toast.LENGTH_SHORT).show();
        }else{
            if (isNumeric(userPhone.getText().toString())){
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
            }else {
                Toast.makeText(getApplicationContext(),"手机号码格式错误",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @OnClick(R.id.registerBtn)
    public void onRegisterBtnClick(){
        if (userPhone.getText().length() == 0 || anlayNum.getText().length() ==0 || password.getText().length() == 0 || username.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"请填写完整信息",Toast.LENGTH_SHORT).show();
        }else {
//                    register();
            SMSSDK.submitVerificationCode("86",userPhone.getText().toString(),anlayNum.getText().toString());
        }
    }



    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private void register(){
        OkHttpUtils.post()
                .url(getString(R.string.baseUrl)+(String)RegisterActivity.this.getResources().getText(R.string.registerUrl))
                .addParams("userPhone",userPhone.getText().toString())
                .addParams("passWord",password.getText().toString())
                .addParams("username",username.getText().toString())
                .addParams("userCompanyName",company.getText().toString())
                .build()
                .execute(new BeanCallBack<ServerBean<RegisterBean>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG,"error");
                    }

                    @Override
                    public void onResponse(ServerBean<RegisterBean> response, int id) {
                        Log.e(TAG,response.toString());
                        if (response.getRet().equalsIgnoreCase("success")){
                            Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),response.getForUser(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
