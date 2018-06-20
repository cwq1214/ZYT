package com.zyt.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.login.LoginActivity;
import com.zyt.util.ConstList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class ResetPasswordActivity extends AppCompatActivity {

    @InjectView(R.id.password)
    EditText password;
    @InjectView(R.id.repeatPassword)
    EditText repeatPassword;

    private String userPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.inject(this);

        userPhoneNum = getIntent().getStringExtra("phoneNum");
    }


    @OnClick({R.id.backBtn, R.id.finishBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.finishBtn:
                if (password.getText().length() == 0 || repeatPassword.getText().length() == 0){
                    Toast.makeText(getApplicationContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
                }else {
                    if (password.getText().toString().equalsIgnoreCase(repeatPassword.getText().toString())){
                        OkHttpUtils.post().url(getString(R.string.baseUrl)+"/server/user/initialization/resetpwd")
                                .addParams("userPhone",userPhoneNum)
                                .addParams("passWord",password.getText().toString())
                                .build()
                                .execute(new BeanCallBack<ServerBean<Object>>() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.e("bee","error");
                                    }

                                    @Override
                                    public void onResponse(ServerBean<Object> response, int id) {
                                        if (response.getRet().equalsIgnoreCase("success")){
                                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
//                                            finish();
                                            ResetPasswordActivity.this.startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(getApplicationContext(),"两次密码不相同",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
