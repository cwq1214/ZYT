package com.zyt.sunpan.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.App;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.base.BaseJSON;
import com.zyt.sunpan.SunpanAdapter;
import com.zyt.sunpan.bean.Sunpan;
import com.zyt.util.ColorDecoration;
import com.zyt.util.ConstList;
import com.zyt.util.ToastUtils;
import com.zyt.util.UserInfo;
import com.zyt.util.Util;
import com.zyt.util.WeChartHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class ShareSunpanActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();

    @InjectView(R.id.textView64)
    TextView textView64;
    @InjectView(R.id.relativeLayout10)
    RelativeLayout relativeLayout10;
    @InjectView(R.id.background_msg)
    TextView backgroundMsg;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.share)
    TextView share;
    @InjectView(R.id.mySP)
    TextView mySp;
    @InjectView(R.id.text_currentLocation)
    TextView textCurrentLocation;

    boolean isOpenSearchInput = false;

    float defaultX = -1;


    SunpanAdapter adapter = new SunpanAdapter();

    SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    //下拉刷新
    boolean isRefreshing = false;

    //上拉加载
    boolean isLoading = false;

    //已经全部加载完
    boolean isAllLoaded = false;

    ArrayList<Sunpan> sunpen = new ArrayList<>();

    @InjectView(R.id.searchET)
    EditText searchET;

    WeChartHelper weChartHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spgx);
        ButterKnife.inject(this);

        initView();
        textCurrentLocation.setText(UserInfo.getUserLocation());
        weChartHelper = new WeChartHelper();
        weChartHelper.init(this, App.weiXin_AppId);
        weChartHelper.registerToWx();
        weChartHelper.setReceiveUserInfoListener(new WeChartHelper.ReceiveUserInfoListener() {
            @Override
            public void onGotUserInfo(WeChartHelper.WxUser user) {
                Logger.d(user);


                OkHttpUtils.post().url(getString(R.string.baseUrl)+getString(R.string.loginUrl2)).addParams("openid",user.getOpenid()).build().execute(new BeanCallBack<String>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort(ShareSunpanActivity.this,"登陆失败");

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ToastUtils.showShort(ShareSunpanActivity.this,"登陆成功");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 200){
                                String userId = jsonObject.getJSONObject("data").getString("userId");
                                Util.setUserID(ShareSunpanActivity.this,userId);
                                UserInfo.setToken(userId);
                            }

                            mySp.setVisibility(code==200?View.VISIBLE:View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });



        mySp.setVisibility(UserInfo.isLogin()?View.VISIBLE:View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weChartHelper.unInit();
    }

    private void initView() {
//        searchET.setFocusable(false);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefreshing) {
                    return;
                }
                isRefreshing = true;
                OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/sunpan/search")
                        .addParams("keyWord", searchET.getText().toString())
                        .addParams("city",UserInfo.getUserLocation())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("bee", "error");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("bee", "search response:" + response);
                                BaseJSON<ArrayList<Sunpan>> baseJSON = new Gson().fromJson(response, new TypeToken<BaseJSON<ArrayList<Sunpan>>>() {
                                }.getType());
                                if (baseJSON.getCode() == 200) {
                                    ArrayList<Sunpan> sunpen = baseJSON.getData();
                                    ShareSunpanActivity.this.sunpen.clear();
                                    ShareSunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();


                                    refreshLayout.setRefreshing(false);

                                    isRefreshing = false;
                                    isAllLoaded = false;

                                } else {
//                    makeToast(baseJSON.getForUser());
                                }
                                if (ShareSunpanActivity.this.sunpen == null || ShareSunpanActivity.this.sunpen.size() == 0) {
                                    backgroundMsg.setVisibility(View.VISIBLE);
                                } else {
                                    backgroundMsg.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        };
        refreshLayout.setOnRefreshListener(onRefreshListener);

        refreshLayout.setColorSchemeColors(Color.parseColor("#ff0000"));
        adapter.setSunpan(sunpen);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ColorDecoration(Color.parseColor("#999999"), 1, LinearLayoutManager.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (isLoading) {
                    return;
                }

                if (isAllLoaded) {
                    makeToast("已经到底部了!");
                    return;
                }
                if (dy <= 0) {
                    return;
                }
                Log.e(TAG, String.valueOf(dy));

                int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastItem == recyclerView.getLayoutManager().getItemCount() - 1) {
                    isLoading = true;
                    getRemoteData(lastItem + "");
                }
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/sunpan/search")
                        .addParams("keyWord", editable.toString())
                        .addParams("city",UserInfo.getUserLocation())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("bee", "error");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e("bee", "search response:" + response);
                                BaseJSON<ArrayList<Sunpan>> baseJSON = new Gson().fromJson(response, new TypeToken<BaseJSON<ArrayList<Sunpan>>>() {
                                }.getType());
                                if (baseJSON.getCode() == 200) {
                                    ArrayList<Sunpan> sunpen = baseJSON.getData();
                                    ShareSunpanActivity.this.sunpen.clear();
                                    ShareSunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();


                                    refreshLayout.setRefreshing(false);

                                    isRefreshing = false;
                                    isAllLoaded = false;

                                } else {
//                    makeToast(baseJSON.getForUser());
                                }
                                if (ShareSunpanActivity.this.sunpen == null || ShareSunpanActivity.this.sunpen.size() == 0) {
                                    backgroundMsg.setVisibility(View.VISIBLE);
                                } else {
                                    backgroundMsg.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }


    private void getRemoteData(final String index) {
        Log.e(TAG, "index " + index);
        OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/sunpan/search")
                .addParams("keyWord", searchET.getText().toString())
                .addParams("city",UserInfo.getUserLocation())
                .addParams("lastId", index).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, e.getMessage());
                        if (ShareSunpanActivity.this.sunpen == null || ShareSunpanActivity.this.sunpen.size() == 0) {
                            backgroundMsg.setVisibility(View.VISIBLE);
                        } else {
                            backgroundMsg.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, response);

                        BaseJSON<ArrayList<Sunpan>> baseJSON = new Gson().fromJson(response, new TypeToken<BaseJSON<ArrayList<Sunpan>>>() {
                        }.getType());

                        if (baseJSON.getCode() == 200) {
                            ArrayList<Sunpan> sunpen = baseJSON.getData();
                            if (index.equals("0")) {
                                ShareSunpanActivity.this.sunpen.clear();
                                ShareSunpanActivity.this.sunpen.addAll(sunpen);
                                adapter.notifyDataSetChanged();


                                refreshLayout.setRefreshing(false);

                                isRefreshing = false;
                                isAllLoaded = false;
                            } else {

                                if (sunpen == null || sunpen.size() == 0) {
                                    isAllLoaded = true;
                                    makeToast("已经到底部了");
                                } else {
                                    ShareSunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();
                                }
                                isLoading = false;
                            }

                        } else {
//                    makeToast(baseJSON.getForUser());
                        }
                        if (ShareSunpanActivity.this.sunpen == null || ShareSunpanActivity.this.sunpen.size() == 0) {
                            backgroundMsg.setVisibility(View.VISIBLE);
                        } else {
                            backgroundMsg.setVisibility(View.GONE);
                        }


                    }
                });
    }


    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.backBtn)
    public void onBackBtnClick() {
        finish();
    }

    @OnClick(R.id.share)
    public void onShareClick() {
        if (UserInfo.isLogin()){
            ReleaseOrUpdateSunpanActivity.launch(this,true,null);
        }else {
            weChartHelper.login();
        }

    }


    @OnClick(R.id.mySP)
    public void onMySpClick(){
        if (UserInfo.isLogin())
            MySunpanActivity.launch(this);
    }

    private void moveAnimaHor(View targetView, float fromX, float toX, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", fromX, toX);
        animator.setDuration(200);
        animator.addListener(listener);
        animator.start();
    }
}
