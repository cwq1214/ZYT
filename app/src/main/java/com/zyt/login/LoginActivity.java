package com.zyt.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.LoginBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.MainActivity;
import com.zyt.R;
import com.zyt.register.ForgetPasswordActivity;
import com.zyt.register.RegisterActivity;
import com.zyt.util.Util;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.userphoneET)
    EditText userphoneET;
    @InjectView(R.id.passwordET)
    EditText passwordET;
    @InjectView(R.id.forgetPassBtn)
    TextView forgetPassBtn;
    @InjectView(R.id.loginBtn)
    Button loginBtn;
    @InjectView(R.id.registerBtn)
    Button registerBtn;
    @InjectView(R.id.imageView4)
    ImageView imageView4;
    @InjectView(R.id.imageView2)
    ImageView imageView2;
    @InjectView(R.id.imageView3)
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        if (!TextUtils.isEmpty(Util.getUserID(this))){
            LoginActivity.this.startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    @OnClick({R.id.forgetPassBtn, R.id.loginBtn, R.id.registerBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgetPassBtn:
                Intent forget = new Intent();
                forget.setClass(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forget);
                break;
            case R.id.loginBtn:
                if (userphoneET.getText().length() == 0 || passwordET.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"请输入手机号和密码",Toast.LENGTH_SHORT).show();
                }else {
                    OkHttpUtils.post().url((String)this.getResources().getText(R.string.baseUrl)+(String)this.getResources().getText(R.string.loginUrl))
                            .addParams("userPhone",userphoneET.getText().toString())
                            .addParams("passWord",passwordET.getText().toString())
                            .build()
                            .execute(new BeanCallBack<ServerBean<LoginBean>>() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("bee","error");
                                    Util.setUserID(getContext(),"");
                                }

                                @Override
                                public void onResponse(ServerBean<LoginBean> response, int id) {
                                    Log.e("bee","response:"+response.toString());
                                    if (response.getRet().equalsIgnoreCase("success")){
                                        Util.setUserID(LoginActivity.this,response.getData().getUserID());
                                        Util.setUserPhone(LoginActivity.this,userphoneET.getText().toString());
                                        Util.setUserPsd(passwordET.getText().toString());

                                        Intent i = new Intent();
                                        i.setClass(LoginActivity.this,MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        Util.setUserID(getContext(),"");
                                        Toast.makeText(getApplicationContext(),response.getForUser(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.registerBtn:
                Intent register = new Intent();
                register.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
                break;
        }
    }

    private Context getContext(){
        return this;
    }
}
