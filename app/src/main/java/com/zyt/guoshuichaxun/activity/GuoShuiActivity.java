package com.zyt.guoshuichaxun.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.GSCXBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.util.ConstList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class GuoShuiActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    @InjectView(R.id.backBtn)
    ImageView backBtn;
    @InjectView(R.id.relativeLayout9)
    RelativeLayout relativeLayout9;
    @InjectView(R.id.input_search)
    EditText inputSearch;
    @InjectView(R.id.search_hint)
    TextView searchHint;
    @InjectView(R.id.search_block)
    LinearLayout searchBlock;
    @InjectView(R.id.mask)
    RelativeLayout mask;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.background_msg)
    TextView backgroundMsg;

    boolean isOpenSearchInput = false;

    float defaultX = -1;

    String searchKey = "";

    private ArrayList<GSCXBean> listData = new ArrayList<GSCXBean>();
    private SearchResultAdapter resultAdapter;
    private boolean canPullUpRefresh = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guoshui);
        ButterKnife.inject(this);

        initView();
    }

    class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public int getItemCount() {
            if (listData==null){
                return 0;
            }else{
                if (listData.size()==0){
                    backgroundMsg.setVisibility(View.VISIBLE);
                }else {
                    backgroundMsg.setVisibility(View.GONE);
                }
                return listData.size();
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SearchResultHolder)holder).setData(listData.get(position));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SearchResultHolder(LayoutInflater.from(GuoShuiActivity.this).inflate(R.layout.layout_gscx_viewholder,parent,false));
        }

        class SearchResultHolder extends RecyclerView.ViewHolder{
            private TextView apartmentName;
            private TextView dong;
            private TextView ceng;
            private TextView price;
            private TextView date;

            public SearchResultHolder (View view){
                super(view);
                apartmentName = (TextView) view.findViewById(R.id.apartmentName);
                dong = (TextView) view.findViewById(R.id.dong);
                ceng = (TextView) view.findViewById(R.id.ceng);
                price = (TextView) view.findViewById(R.id.price);
                date = (TextView) view.findViewById(R.id.date);
            }

            public void setData(GSCXBean bean){
                apartmentName.setText(bean.getTpBuildingName());
                dong.setText(bean.getTpBuildingNo());
                ceng.setText(bean.getTpFloor());
                price.setText(bean.getTpPrice().toString()+"元/平方米");
                date.setText(bean.getTpDate());
            }
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(5));
        recyclerView.setAdapter(resultAdapter = new SearchResultAdapter());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = recyclerView.getLayoutManager().getChildCount();
                int total = recyclerView.getLayoutManager().getItemCount();
                int past= ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total && canPullUpRefresh){
                    canPullUpRefresh = false;
                    OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/taxPrice/search")
                            .addParams("lastId", listData.get(listData.size()-1).getTaxPriceId())
                            .addParams("keyWord",searchKey)
                            .build()
                            .execute(new BeanCallBack<ServerBean<ArrayList<GSCXBean>>>() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(ServerBean<ArrayList<GSCXBean>> response, int id) {
                                    Log.e("bee","response:"+response.toString());
                                    if (response.getData().size()==0){
                                        Toast.makeText(getApplicationContext(),"已经没有数据了",Toast.LENGTH_SHORT).show();
                                    }else{
                                        listData.addAll(response.getData());
                                        resultAdapter.notifyDataSetChanged();
                                        canPullUpRefresh = true;
                                    }
                                }
                            });
                }
            }
        });

        OkHttpUtils.get().url(getString(R.string.baseUrl)+"/server/taxPrice/list")
                .build()
                .execute(new BeanCallBack<ServerBean<ArrayList<GSCXBean>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("bee","error");
                    }

                    @Override
                    public void onResponse(ServerBean<ArrayList<GSCXBean>> response, int id) {
                        listData = response.getData();
                        resultAdapter.notifyDataSetChanged();
                    }
                });


        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, editable.toString());
                searchKey = editable.toString();
                getRemoteData(editable.toString(), "0");
            }
        });

        refreshLayout.setColorSchemeColors(Color.parseColor("#ff0000"));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (TextUtils.isEmpty(searchKey)) {
//                    makeToast("请输入搜索内容");
//                    refreshLayout.setRefreshing(false);
//                    return;
//                }
                getRemoteData(searchKey, "0");
            }
        });

        //TODO recyclerview
    }


    private void getRemoteData(String key, String index) {
        //TODO 添加属性
        OkHttpUtils.get().url(getString(R.string.baseUrl) + "/server/taxPrice/search?")
                .addParams("lastId", index)
                .addParams("keyWord",key)
                .build()
                .execute(new BeanCallBack<ServerBean<ArrayList<GSCXBean>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ServerBean<ArrayList<GSCXBean>> response, int id) {
                        listData = response.getData();
                        resultAdapter.notifyDataSetChanged();

                        refreshLayout.setRefreshing(false);

                        canPullUpRefresh = true;
                    }
                });

    }

    @OnClick(R.id.mask)
    public void onMaskClick() {

        if (!isOpenSearchInput) {
            Log.e(TAG, "in");
            if (defaultX == -1) {
                defaultX = searchBlock.getX();

            }
            moveAnimaHor(searchBlock, 0, -defaultX+10, new Animator.AnimatorListener() {
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
            inputSearch.setFocusableInTouchMode(true);
            inputSearch.setFocusable(true);
            inputSearch.requestFocus();
            mask.setClickable(false);
        }
    }


    private void moveAnimaHor(View targetView, float fromX, float toX, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", fromX, toX);
        animator.setDuration(200);
        animator.addListener(listener);
        animator.start();
    }

    private void viewAlphaAnima(View targetView, float fromP, float toP) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "alpha", fromP, toP).setDuration(200);

        objectAnimator.start();
    }


    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.backBtn)
    public void onClick() {
        finish();
    }
}
