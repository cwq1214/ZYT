package com.zyt.sunpan.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
import com.zyt.sunpan.SunpanAdapter;
import com.zyt.sunpan.bean.Sunpan;
import com.zyt.util.ColorDecoration;
import com.zyt.util.ConstList;

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
    @InjectView(R.id.backBtn)
    ImageView backBtn;
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


    @InjectView(R.id.search_hint)
    TextView searchHint;
    @InjectView(R.id.search_block)
    LinearLayout searchBlock;
    @InjectView(R.id.mask)
    RelativeLayout mask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spgx);
        ButterKnife.inject(this);

        initView();

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    private void initView() {
        searchET.setFocusable(false);
        onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isRefreshing) {
                    return;
                }
                isRefreshing = true;
                OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/search")
                        .addParams("keyWord", searchET.getText().toString())
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
                OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/search")
                        .addParams("keyWord", editable.toString())
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
        OkHttpUtils.get().url(ConstList.domain + "/server/sunpan/search")
                .addParams("keyWord", searchET.getText().toString())
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
        ReleaseOrUpdateSunpanActivity.launch(this,true,null);
    }

    @OnClick(R.id.mask)
    public void onMaskClick() {

        if (!isOpenSearchInput) {
            Log.e(TAG, "in");
            if (defaultX == -1) {
                defaultX = searchBlock.getX();

            }
            moveAnimaHor(searchBlock, 0, -defaultX + 10, new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    searchHint.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            isOpenSearchInput = true;
            searchET.setFocusableInTouchMode(true);
            searchET.setFocusable(true);
            searchET.requestFocus();
            mask.setClickable(false);
        }
    }
    @OnClick(R.id.mySP)
    public void onMySpClick(){
        MySunpanActivity.launch(this);
    }

    private void moveAnimaHor(View targetView, float fromX, float toX, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", fromX, toX);
        animator.setDuration(200);
        animator.addListener(listener);
        animator.start();
    }
}
