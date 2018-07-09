package com.zyt.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.HttpUtil.Bean.LoginBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.MainActivity;
import com.zyt.R;
import com.zyt.util.Util;

import okhttp3.Call;

/**
 * Created by v7 on 2016/8/30.
 */
public class WelcomePageActivity extends AppCompatActivity {

    String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }



        ImageView imageView = new ImageView(this);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.welcomepage2));

        setContentView(imageView);

//        autoLogin();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               finish();
                startActivity(new Intent(WelcomePageActivity.this,MainActivity.class));
            }
        },3000);

    }

    Handler handler = new Handler();

    private void autoLogin(){
        if (!TextUtils.isEmpty(Util.getUserPhone(this))&&!TextUtils.isEmpty(Util.getUserPsd())){
            login(Util.getUserPhone(getContext()),Util.getUserPsd());
        }else {
            Util.setUserID(this,"");
        }
    }

    private void login(final String userName, final String password){
        OkHttpUtils.post().url((String)this.getResources().getText(R.string.baseUrl)+(String)this.getResources().getText(R.string.loginUrl))
                .addParams("userPhone",userName)
                .addParams("passWord",password)
                .build()
                .execute(new BeanCallBack<ServerBean<LoginBean>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","error");
                        Util.setUserID(getContext(),"");
                    }

                    @Override
                    public void onResponse(ServerBean<LoginBean> response, int id) {
                        Log.e("TAG","response:"+response.toString());
                        if (response.getRet().equalsIgnoreCase("success")){
                            Util.setUserID(getContext(),response.getData().getUserID());
                            Util.setUserPhone(getContext(),userName);
                            Util.setUserPsd(password);
                        }else {
                            Util.setUserID(getContext(),"");
                            Toast.makeText(getApplicationContext(),response.getForUser(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Context getContext(){
        return this;
    }
}
