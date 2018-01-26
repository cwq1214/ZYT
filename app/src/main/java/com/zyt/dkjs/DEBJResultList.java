package com.zyt.dkjs;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.zyt.HttpUtil.Bean.MYCHBean;
import com.zyt.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DEBJResultList extends AppCompatActivity {

    @InjectView(R.id.detailList)
    RecyclerView detailList;

    private ResultAdapter resultAdapter;
    private ArrayList<MYCHBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debjresult_list);
        ButterKnife.inject(this);

        dataList = (ArrayList<MYCHBean>)getIntent().getSerializableExtra("myhkData");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        detailList.setLayoutManager(manager);
        detailList.setAdapter(resultAdapter = new ResultAdapter());
        detailList.addItemDecoration(new SpaceItemDecoration(5));

        resultAdapter.notifyDataSetChanged();

        Log.e("bee",dataList.toString());
    }



    @OnClick(R.id.backBtn)
    public void onClick() {
        finish();
    }

    class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public int getItemViewType(int position) {
            if (position%13==0){
                return 0;
            }else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size()+dataList.size()/12;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==0){
                return new ResultHeaderHolder(LayoutInflater.from(DEBJResultList.this).inflate(R.layout.debj_header_layout,parent,false));
            }else {
                return new ResultHolder(LayoutInflater.from(DEBJResultList.this).inflate(R.layout.debj_layout,parent,false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ResultHolder){
                ((ResultHolder) holder).setData(dataList.get(position-((int)position/13+1)));
            }else if (holder instanceof ResultHeaderHolder){
                ((ResultHeaderHolder) holder).setData(position/13);
            }
        }

        class ResultHolder extends RecyclerView.ViewHolder{
            TextView qs;
            TextView chlx;
            TextView chbj;
            TextView sybj;

            public ResultHolder(View view){
                super(view);
                qs = (TextView) view.findViewById(R.id.qs);
                chlx = (TextView) view.findViewById(R.id.chlx);
                chbj = (TextView) view.findViewById(R.id.chbj);
                sybj = (TextView) view.findViewById(R.id.sybj);
            }

            public void setData(MYCHBean bean){
                DecimalFormat df = new DecimalFormat(",###.##");

                qs.setText(Integer.toString(bean.getMonth()%12==0?12:bean.getMonth()%12));
                chlx.setText(df.format(bean.getChlx()));
                chbj.setText(df.format(bean.getChbj()));
                sybj.setText(df.format(bean.getSybj()));


            }
        }

        class ResultHeaderHolder extends RecyclerView.ViewHolder{
            TextView year;

            public ResultHeaderHolder (View view){
                super(view);
                year = (TextView) view.findViewById(R.id.year);
            }

            public void setData (int y){
                year.setText("第"+(y+1)+"年");
            }
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

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
}
