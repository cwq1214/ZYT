package com.zyt.clp;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.OkHttpClient;

/**
 * Created by chenbifeng on 16/8/4.
 */
public class PanoApplication extends Application {
    private static PanoApplication mInstance = null;
    public BMapManager bMapManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initEngineManager(this);

        Hawk.init(this).setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setPassword("23333")
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
        OkHttpUtils.initClient(new OkHttpClient());
    }

    public void initEngineManager(Context context){
        if (bMapManager == null){
            bMapManager = new BMapManager(context);
        }

//        if (!bMapManager.init(new MyGeneralListener())){
//            Toast.makeText(PanoApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
//                    Toast.LENGTH_LONG).show();
//        }
    }

    public static PanoApplication getInstance(){
        return mInstance;
    }


    static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Toast.makeText(PanoApplication.getInstance().getApplicationContext(),
                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(PanoApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

}
