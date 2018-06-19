package com.zyt.sunpan.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.R;
import com.zyt.base.BaseJSON;
import com.zyt.sunpan.MySunpanAdapter;
import com.zyt.sunpan.MySunpanViewHolder;
import com.zyt.sunpan.SunpanAdapter;
import com.zyt.sunpan.bean.Sunpan;
import com.zyt.util.ColorDecoration;
import com.zyt.util.ConstList;
import com.zyt.util.ToastUtils;
import com.zyt.util.Util;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by v7 on 2016/8/24.
 */
public class MySunpanActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();


    boolean isOpenSearchInput = false;

    float defaultX = -1;


    MySunpanAdapter adapter = new MySunpanAdapter();

    SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    //下拉刷新
    boolean isRefreshing = false;

    //上拉加载
    boolean isLoading = false;

    //已经全部加载完
    boolean isAllLoaded = false;

    ArrayList<Sunpan> sunpen = new ArrayList<>();
    @InjectView(R.id.textView64)
    TextView textView64;

    @InjectView(R.id.relativeLayout10)
    RelativeLayout relativeLayout10;
    @InjectView(R.id.searchET)
    EditText searchET;
    @InjectView(R.id.background_msg)
    TextView backgroundMsg;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_sunpan);
        ButterKnife.inject(this);

        initView();


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

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MySunpanActivity.class));
    }

    public void initView() {
        adapter.setOnDelClickListener(new MySunpanViewHolder.OnDelClickListener() {
            @Override
            public void onDelClick(int position) {
                Sunpan sunpan = sunpen.get(position);
                delMySunpan(sunpan.getSunPanId());

            }
        });
//        searchET.setFocusable(false);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefreshing) {
                    return;
                }
                isRefreshing = true;
                OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/search")
                        .addParams("keyWord", searchET.getText().toString())
                        .addParams("userId",Util.getUserID(getBaseContext()))
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
                                    MySunpanActivity.this.sunpen.clear();
                                    MySunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();


                                    refreshLayout.setRefreshing(false);

                                    isRefreshing = false;
                                    isAllLoaded = false;

                                } else {
//                    makeToast(baseJSON.getForUser());
                                }
                                if (MySunpanActivity.this.sunpen == null || MySunpanActivity.this.sunpen.size() == 0) {
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
                OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/search")
                        .addParams("keyWord", editable.toString())
                        .addParams("userId", Util.getUserID(getBaseContext()))
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
                                    MySunpanActivity.this.sunpen.clear();
                                    MySunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();


                                    refreshLayout.setRefreshing(false);

                                    isRefreshing = false;
                                    isAllLoaded = false;

                                } else {
//                    makeToast(baseJSON.getForUser());
                                }
                                if (MySunpanActivity.this.sunpen == null || MySunpanActivity.this.sunpen.size() == 0) {
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
        OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/self")
                .addParams("userId", Util.getUserID(getBaseContext()))
                .addParams("lastId", index).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, e.getMessage());
                        if (MySunpanActivity.this.sunpen == null || MySunpanActivity.this.sunpen.size() == 0) {
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
                                MySunpanActivity.this.sunpen.clear();
                                MySunpanActivity.this.sunpen.addAll(sunpen);
                                adapter.notifyDataSetChanged();


                                refreshLayout.setRefreshing(false);

                                isRefreshing = false;
                                isAllLoaded = false;
                            } else {

                                if (sunpen == null || sunpen.size() == 0) {
                                    isAllLoaded = true;
                                    makeToast("已经到底部了");
                                } else {
                                    MySunpanActivity.this.sunpen.addAll(sunpen);
                                    adapter.notifyDataSetChanged();
                                }
                                isLoading = false;
                            }

                        } else {
//                    makeToast(baseJSON.getForUser());
                        }
                        if (MySunpanActivity.this.sunpen == null || MySunpanActivity.this.sunpen.size() == 0) {
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


    private void delMySunpan(final String sunpanId){
        Log.e(TAG,sunpanId);
        OkHttpUtils.post().url(ConstList.domain+"/server/sunpan/delete").addParams("sunPanId",sunpanId).addParams("userId",Util.getUserID(getBaseContext())).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG,e.getMessage());

                ToastUtils.showShort(getBaseContext(),"网络错误");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG,response);
                // {"data": {}, "ret": "success", "code": 200, "forUser": "", "forWorker": "", "version": "1.0"}
                BaseJSON json = new Gson().fromJson(response,new TypeToken<BaseJSON>(){}.getType());
                if(json.getCode()==200&&json.getRet().equals("success")){
                    for (int i=0;i<sunpen.size();i++){
                        if (sunpen.get(i).getSunPanId().equals(sunpanId)){
                            sunpen.remove(i);
                            adapter.notifyDataSetChanged();
                            if (sunpen.size()==0){
                                backgroundMsg.setVisibility(View.VISIBLE);
                            }else {
                                backgroundMsg.setVisibility(View.GONE);
                            }

                            break;
                        }
                    }
                }else {
                    ToastUtils.showShort(MySunpanActivity.this, TextUtils.isEmpty(json.getForUser())?"删除失败":json.getForUser());
                }

            }
        });
    }

    private void moveAnimaHor(View targetView, float fromX, float toX, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", fromX, toX);
        animator.setDuration(200);
        animator.addListener(listener);
        animator.start();
    }

}
