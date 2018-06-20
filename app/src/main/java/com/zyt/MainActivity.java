package com.zyt;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.base.BaseJSON;
import com.zyt.base.City;
import com.zyt.base.DownLoadFragmentDialog;
import com.zyt.base.Province;
import com.zyt.clp.CLPActivity;
import com.zyt.dkjs.DKJSActivity;
import com.zyt.login.LoginActivity;
import com.zyt.lxwm.LXWMActivity;
import com.zyt.spjsq.SPJSQActivity;
import com.zyt.sunpan.activity.ShareSunpanActivity;
import com.zyt.util.ConstList;
import com.zyt.util.LocationUtil;
import com.zyt.util.ScreenUtils;
import com.zyt.util.ToastUtils;
import com.zyt.util.UserInfo;
import com.zyt.util.Util;
import com.zyt.util.WeChartHelper;
import com.zyt.zzjsq.ZZJSQActivity;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();


    String userId;
    @InjectView(R.id.logoutBtn)
    TextView logoutBtn;
    @InjectView(R.id.text_currentLocation)
    TextView textCurrentLocation;
    @InjectView(R.id.btn_sel_location)
    LinearLayout btnSelLocation;
    @InjectView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @InjectView(R.id.banner)
    Banner banner;
    @InjectView(R.id.btn_spjsq)
    LinearLayout btnSpjsq;
    @InjectView(R.id.btn_zzjsq)
    LinearLayout btnZzjsq;
    @InjectView(R.id.btn_dkjsq)
    LinearLayout btnDkjsq;
    @InjectView(R.id.btn_spgx)
    LinearLayout btnSpgx;
    @InjectView(R.id.btn_clp)
    LinearLayout btnClp;
    @InjectView(R.id.btn_lxwm)
    LinearLayout btnLxwm;

    public LocationClient mLocationClient = null;

    private List<Province> options1Items = new ArrayList<>();
    private List<List<City>> options2Items = new ArrayList<>();

    private List<com.zyt.base.Banner> banners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        userId = Util.getUserID(this);

        OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/version/now")
                .build()
                .execute(new BeanCallBack<ServerBean<Map<String, String>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG + "error", e.getMessage());
                    }

                    @Override
                    public void onResponse(final ServerBean<Map<String, String>> response, int id) {
                        Log.e(TAG + "response", response.toString());
                        if (response.getData().keySet().size() != 0) {
                            try {
                                if (response.getData().get("version").compareToIgnoreCase(MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), 0).versionName) > 0) {
                                    //                                Toast.makeText(MainActivity.this, "有新版本", Toast.LENGTH_SHORT).show();

                                    if (!TextUtils.isEmpty(response.getData().get("url"))) {
                                        new AlertDialog.Builder(MainActivity.this).setMessage("发现新版本，是否更新？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String version = response.getData().get("version");
                                                String url = response.getData().get("url");
                                                File file = new File((Environment.getExternalStorageDirectory().getAbsolutePath() + "/fms_" + version + ".apk"));
                                                if (file.exists()) {
                                                    openFile(file);
                                                } else {
                                                    downLoadApk(version, url);
                                                }
                                                dialogInterface.dismiss();
                                            }
                                        }).show();


                                    } else {

                                    }
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });


        mLocationClient = new LocationClient(getApplicationContext());

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener((BDLocation location)-> {
                String addr = location.getAddrStr();    //获取详细地址信息
                String country = location.getCountry();    //获取国家
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息
                if (city!=null) {
                    if (city.endsWith("市")){

                    }
                    String cityName = city.substring(0,city.lastIndexOf("市"));
                    textCurrentLocation.setText(city);
                    getBanner();
//                    if (!cityName.equals(Hawk.get("lc"))){
                        new AlertView.Builder().setContext(this)
                                .setStyle(AlertView.Style.Alert)
                                .setOnItemClickListener((Object o, int position)-> {
                                    if (position==-1){
                                        showSelLocationDialog();
                                    }else if (position==0){
                                        setLocationText(cityName);
                                    }
                                })
                                .setTitle("")
                                .setCancelText("否")
                                .setDestructive("是")
                                .setMessage("系统已自动为你定位\n" +
                                        "是否正确")

                                .build().show();
//                    }
                    mLocationClient.stop();

                }
        });



        int width = ScreenUtils.getScreenWidth(MainActivity.this);
        banner.setLayoutParams(new LinearLayout.LayoutParams(width,width*130/375));

        banner.setOnBannerListener(position -> {
            String tel = banners.get(position).getaMobile();

            new AlertView.Builder().setContext(this)
                    .setStyle(AlertView.Style.Alert)
                    .setOnItemClickListener((Object o, int btnposition)-> {
                        if (btnposition==-1){

                        }else if (btnposition==0){
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:"+tel));
                            startActivity(intent);
                        }
                    })
                    .setTitle("拨打号码")
                    .setCancelText("取消")
                    .setDestructive("拨打")
                    .setMessage(tel)

                    .build().show();




        });

        String cityName = UserInfo.getUserLocation();
        if (TextUtils.isEmpty(cityName)){
            mLocationClient.start();
        }else {
            textCurrentLocation.setText(cityName);
            getBanner();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        logoutBtn.setVisibility(UserInfo.isLogin()?View.VISIBLE:View.GONE);

    }

    public void getBanner(){
        OkHttpUtils.get().url(getString(R.string.baseUrl)+"/server/request/list_ad?spell="+ URLEncoder.encode(textCurrentLocation.getText().toString()) ).build().execute(new BeanCallBack<BaseJSON<List<com.zyt.base.Banner>>>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseJSON<List<com.zyt.base.Banner>> response, int id) {
                System.out.println(response.getRet());
                if (response.getRet().equals("success")){
                    banners = response.getData();
                    List<String> images=new ArrayList<>();

                    for (int i=0;i<banners.size();i++){
                        images.add(banners.get(i).getaImg());
                    }
                    banner.setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            Glide.with(context).load(path).into(imageView);
                        }
                    });

                    banner.setImages(images);
                    banner.start();
                }
            }
        });
    }

    public void showSelLocationDialog(){

        options1Items = LocationUtil.getInstance().getProvinces();

        for (int i=0;i<options1Items.size();i++){
            options2Items.add(options1Items.get(i).getCities());
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(MainActivity.this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置

            String cityName =  options2Items.get(options1).get(option2).getPickerViewText();
            if (TextUtils.isEmpty(cityName)){
                cityName =  options1Items.get(options1).getPickerViewText();
            }
            setLocationText(cityName);
            getBanner();
        }).build();
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }


    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void downLoadApk(String version, String url) {
        final DownLoadFragmentDialog downLoadFragmentDialog = new DownLoadFragmentDialog();
        downLoadFragmentDialog.show(getSupportFragmentManager(), "");
        downLoadFragmentDialog.setText("开始下载");
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "zyt_" + version + ".apk") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("download error ", e.getMessage());

                downLoadFragmentDialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                Log.e("doanload progress ", String.valueOf(progress));

                downLoadFragmentDialog.setText("当前进度" + (int) (progress * 100) + "%");
            }

            @Override
            public void onResponse(File response, int id) {
                Log.e("download finish ", "finish");
                openFile(response);
                downLoadFragmentDialog.dismiss();
            }
        });
    }


    @OnClick({R.id.btn_sel_location,R.id.btn_spjsq,R.id.btn_zzjsq,R.id.btn_dkjsq,R.id.btn_spgx,R.id.btn_clp,R.id.btn_lxwm,R.id.logoutBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sel_location:
                //选择地址
                showSelLocationDialog();
                break;
            case R.id.btn_spjsq:
                Log.e("bee","商铺计税器");
                Intent spjstI = new Intent();
                spjstI.setClass(MainActivity.this, SPJSQActivity.class);
                startActivity(spjstI);
                break;
            case R.id.btn_zzjsq:
                Log.e("bee","住宅计税器");
                Intent zzjsqI = new Intent();
                zzjsqI.setClass(MainActivity.this, ZZJSQActivity.class);
                startActivity(zzjsqI);
                break;
            case R.id.btn_dkjsq:
                Log.e("bee","贷款计算");
                Intent dkjsI = new Intent();
                dkjsI.setClass(MainActivity.this, DKJSActivity.class);
                startActivity(dkjsI);
                break;
            case R.id.btn_spgx:
                Log.e("bee","笋盘共享");
                Intent spgx = new Intent(MainActivity.this, ShareSunpanActivity.class);
                MainActivity.this.startActivity(spgx);
                break;
//            case R.id.gsjcx:
//                Log.e("bee","过税价查询");
//                MainActivity.this.startActivity(new Intent(MainActivity.this, GuoShuiActivity.class));
//                break;
            case R.id.btn_clp:
                Log.e("bee","查楼盘");
                Intent clpI = new Intent();
                clpI.setClass(MainActivity.this, CLPActivity.class);
                startActivity(clpI);
                break;
//            case R.id.wysl:
////                Log.e("bee","我要赎楼");
//                startActivity(new Intent(this,BuDongChanChaXunActivity.class));
//
//                break;
//            case R.id.wydk:
//                Log.e("bee","我要贷款");
//                MainActivity.this.startActivity(new Intent(MainActivity.this, WYSLActivity.class));
//
////                MainActivity.this.startActivity(new Intent(MainActivity.this, WYDKActivity.class));
//                break;
            case R.id.btn_lxwm:
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
                UserInfo.setToken(null);

                ToastUtils.showShort(MainActivity.this,"已退出登陆");

                logoutBtn.setVisibility(View.GONE);
//                finish();
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
        }
    }


    Handler handler = new Handler();


    boolean close = false;

    @Override
    public void onBackPressed() {
        if (close) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            close = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    close = false;
                }
            }, 2000);
        }
    }

    public void setLocationText(String text){
        textCurrentLocation.setText(text);
        UserInfo.setUserLocation(text);
    }

    @Override
    protected void onDestroy() {

        OkHttpUtils.post().url(getString(R.string.baseUrl) + "/server/user/initialization/signOut").addParams("userId", userId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, response);
            }
        });
        super.onDestroy();

    }
}
