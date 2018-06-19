package com.zyt;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.util.LocationUtil;
import com.zyt.util.WeChartHelper;

import okhttp3.OkHttpClient;

/**
 * Created by chenbifeng on 16/8/4.
 */
public class App extends Application {
    private static App mInstance = null;
    public static String weiXin_AppId = "wx739c623989ed0ebb";
    public static String weiXin_AppSecret = "632675351f1e51bf05d2079d4b68dd6d";
    public BMapManager bMapManager = null;

    
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initEngineManager(this);

        SDKInitializer.initialize(getApplicationContext());


        Hawk.init(this).build();


        OkHttpUtils.initClient(new OkHttpClient());

        Logger.addLogAdapter(new AndroidLogAdapter());

        LocationUtil.getInstance().init();


    }

    public void initEngineManager(Context context){
        if (bMapManager == null){
            bMapManager = new BMapManager(context);
        }
//        if (!bMapManager.init(new MyGeneralListener())){
//            Toast.makeText(App.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
//                    Toast.LENGTH_LONG).show();
//        }
    }

    public static App getInstance(){
        return mInstance;
    }


    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Toast.makeText(App.getInstance().getApplicationContext(),
                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(App.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}
