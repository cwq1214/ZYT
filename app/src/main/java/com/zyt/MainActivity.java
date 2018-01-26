package com.zyt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.base.DownLoadFragmentDialog;
import com.zyt.bdccx.BuDongChanChaXunActivity;
import com.zyt.clp.CLPActivity;
import com.zyt.WYSL.WYSLActivity;
import com.zyt.dkjs.DKJS;
import com.zyt.guoshuichaxun.activity.GuoShuiActivity;
import com.zyt.login.LoginActivity;
import com.zyt.lxwm.LXWMActivity;
import com.zyt.spjsq.SPJSQActivity;
import com.zyt.sunpan.activity.ShareSunpanActivity;
import com.zyt.util.ConstList;
import com.zyt.util.Util;
import com.zyt.zzjsq.ZZJSQ;

import java.io.File;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    @InjectView(R.id.spjsq)
    ImageView spjsq;
    @InjectView(R.id.zzjsq)
    ImageView zzjsq;
    @InjectView(R.id.dkjs)
    ImageView dkjs;
    @InjectView(R.id.spgx)
    ImageView spgx;
    @InjectView(R.id.gsjcx)
    ImageView gsjcx;
    @InjectView(R.id.clp)
    ImageView clp;
    @InjectView(R.id.wysl)
    ImageView wysl;
    @InjectView(R.id.wydk)
    ImageView wydk;
    @InjectView(R.id.lxwm)
    ImageView lxwm;


    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        userId = Util.getUserID(this);

        try {
            Log.e(TAG,"当前版本:"+MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        OkHttpUtils.get().url(ConstList.domain+"/server/version/now")
                .build()
                .execute(new BeanCallBack<ServerBean<Map<String,String>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG+"error",e.getMessage());
                    }

                    @Override
                    public void onResponse(final ServerBean<Map<String, String>> response, int id) {
                        Log.e(TAG+"response",response.toString());
                        if (response.getData().keySet().size()!=0){
                            try {
                                if (response.getData().get("version").compareToIgnoreCase(MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(),0).versionName)>0){
    //                                Toast.makeText(MainActivity.this, "有新版本", Toast.LENGTH_SHORT).show();

                                    if (!TextUtils.isEmpty(response.getData().get("url"))){
                                        new AlertDialog.Builder(MainActivity.this).setMessage("发现新版本，是否更新？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String version=response.getData().get("version");
                                                String url = response.getData().get("url");
                                                File file =new File((Environment.getExternalStorageDirectory().getAbsolutePath()+"/fms_"+version+".apk"));
                                                if (file.exists()){
                                                    openFile(file);
                                                }else {
                                                    downLoadApk(version, url);
                                                }
                                                dialogInterface.dismiss();
                                            }
                                        }).show();


                                    }else {

                                    }
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void downLoadApk(String version,String url){
        final DownLoadFragmentDialog downLoadFragmentDialog = new DownLoadFragmentDialog();
        downLoadFragmentDialog.show(getSupportFragmentManager(),"");
        downLoadFragmentDialog.setText("开始下载");
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"zyt_"+version+".apk") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("download error ",e.getMessage());

                downLoadFragmentDialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                Log.e("doanload progress ", String.valueOf(progress));

                downLoadFragmentDialog.setText("当前进度"+(int)(progress*100)+"%");
            }

            @Override
            public void onResponse(File response, int id) {
                Log.e("download finish ","finish");
                openFile(response);
                downLoadFragmentDialog.dismiss();
            }
        });
    }


    @OnClick({R.id.spjsq, R.id.zzjsq, R.id.dkjs, R.id.spgx, R.id.gsjcx, R.id.clp, R.id.wysl, R.id.wydk, R.id.lxwm ,R.id.logoutBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spjsq:
                Log.e("bee","商铺计税器");
                Intent spjstI = new Intent();
                spjstI.setClass(MainActivity.this, SPJSQActivity.class);
                startActivity(spjstI);
                break;
            case R.id.zzjsq:
                Log.e("bee","住宅计税器");
                Intent zzjsqI = new Intent();
                zzjsqI.setClass(MainActivity.this, ZZJSQ.class);
                startActivity(zzjsqI);
                break;
            case R.id.dkjs:
                Log.e("bee","贷款计算");
                Intent dkjsI = new Intent();
                dkjsI.setClass(MainActivity.this, DKJS.class);
                startActivity(dkjsI);
                break;
            case R.id.spgx:
                Log.e("bee","笋盘共享");
                Intent spgx = new Intent(MainActivity.this, ShareSunpanActivity.class);
                MainActivity.this.startActivity(spgx);
                break;
            case R.id.gsjcx:
                Log.e("bee","过税价查询");
                MainActivity.this.startActivity(new Intent(MainActivity.this, GuoShuiActivity.class));
                break;
            case R.id.clp:
                Log.e("bee","查楼盘");
                Intent clpI = new Intent();
                clpI.setClass(MainActivity.this, CLPActivity.class);
                startActivity(clpI);
                break;
            case R.id.wysl:
//                Log.e("bee","我要赎楼");
                startActivity(new Intent(this,BuDongChanChaXunActivity.class));

                break;
            case R.id.wydk:
                Log.e("bee","我要贷款");
                MainActivity.this.startActivity(new Intent(MainActivity.this, WYSLActivity.class));

//                MainActivity.this.startActivity(new Intent(MainActivity.this, WYDKActivity.class));
                break;
            case R.id.lxwm:
                Log.e("bee","联系我们");
                Intent lxwmI = new Intent();
                lxwmI.setClass(MainActivity.this, LXWMActivity.class);
                startActivity(lxwmI);
                break;
            case R.id.logoutBtn:
                Log.e("bee","登出");
                Util.resetUserID(MainActivity.this);
                Util.setUserID(this,"");
                Util.setUserPsd("");

                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
    }


    Handler handler = new Handler();


    boolean close = false;
    @Override
    public void onBackPressed() {
        if (close) {
            super.onBackPressed();
        }else {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            close = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    close = false;
                }
            },2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.post().url(ConstList.domain+"/server/user/initialization/signOut").addParams("userId",userId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG,response);
            }
        });
    }
}
