package com.zyt.dkjs;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.LoanTaxBean;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.MainActivity;
import com.zyt.R;
import com.zyt.dkjs.fragment.GJJDK;
import com.zyt.dkjs.fragment.SYDK;
import com.zyt.dkjs.fragment.ZHDK;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class DKJS extends AppCompatActivity {

    @InjectView(R.id.sydk)
    TextView sydk;
    @InjectView(R.id.gjjdk)
    TextView gjjdk;
    @InjectView(R.id.zhdk)
    TextView zhdk;
    @InjectView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;

    private SYDK sydkFragment;
    private GJJDK gjjdkFragment;
    private ZHDK zhdkFragment;

    private LoanTaxBean loanTaxBean;

    private CharSequence[] items= {"0.85","0.9","0.95","1","1.05","1.1","1.15","1.2","1.25","1.3"};
    private CharSequence[] showItems= {"2016年1月1日基准利率(0.85)","2016年1月1日基准利率(0.9)","2016年1月1日基准利率(0.95)","2016年1月1日基准利率(1.00)","2016年1月1日基准利率(1.05)"
            ,"2016年1月1日基准利率(1.1)","2016年1月1日基准利率(1.15)","2016年1月1日基准利率(1.2)","2016年1月1日基准利率(1.25)","2016年1月1日基准利率(1.3)"};

    public LoanTaxBean getLoanTaxBean() {
        return loanTaxBean;
    }

    public void setLoanTaxBean(LoanTaxBean loanTaxBean) {
        this.loanTaxBean = loanTaxBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dkjs);
        ButterKnife.inject(this);

        OkHttpUtils.get().url((String)this.getResources().getText(R.string.baseUrl)+(String)this.getResources().getText(R.string.loanTax))
                .build()
                .execute(new BeanCallBack<ServerBean<LoanTaxBean>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ServerBean<LoanTaxBean> response, int id) {
                        Log.e("bee",response.toString());
                        loanTaxBean = response.getData();
                    }
                });

        sydkFragment = (SYDK) getFragmentManager().findFragmentById(R.id.sydkFragment);
        gjjdkFragment = (GJJDK) getFragmentManager().findFragmentById(R.id.gjjdkFragment);
        zhdkFragment = (ZHDK) getFragmentManager().findFragmentById(R.id.zhdkFragment);

        FragmentTransaction defaultTran = getFragmentManager().beginTransaction();
        defaultTran.hide(zhdkFragment);
        defaultTran.hide(gjjdkFragment);
        defaultTran.show(sydkFragment);
        defaultTran.commit();
    }

    @OnClick({R.id.backBtn, R.id.sydk, R.id.gjjdk, R.id.zhdk, R.id.caculateBtn, R.id.resetBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.sydk:
                FragmentTransaction sydkTran = getFragmentManager().beginTransaction();
                sydkTran.hide(zhdkFragment);
                sydkTran.hide(gjjdkFragment);
                sydkTran.show(sydkFragment);
                sydkTran.commit();

                sydk.setBackgroundColor(Color.parseColor("#aaaaaa"));
                gjjdk.setBackgroundColor(Color.parseColor("#dddddd"));
                zhdk.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
            case R.id.gjjdk:
                FragmentTransaction gjjdkTran = getFragmentManager().beginTransaction();
                gjjdkTran.hide(sydkFragment);
                gjjdkTran.hide(zhdkFragment);
                gjjdkTran.show(gjjdkFragment);
                gjjdkTran.commit();

                sydk.setBackgroundColor(Color.parseColor("#dddddd"));
                gjjdk.setBackgroundColor(Color.parseColor("#aaaaaa"));
                zhdk.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
            case R.id.zhdk:
                FragmentTransaction zhdkTran = getFragmentManager().beginTransaction();
                zhdkTran.hide(sydkFragment);
                zhdkTran.hide(gjjdkFragment);
                zhdkTran.show(zhdkFragment);
                zhdkTran.commit();

                sydk.setBackgroundColor(Color.parseColor("#dddddd"));
                gjjdk.setBackgroundColor(Color.parseColor("#dddddd"));
                zhdk.setBackgroundColor(Color.parseColor("#aaaaaa"));
                break;
            case R.id.caculateBtn:
                if (sydkFragment.isVisible()){
                    sydkFragment.caculate();
                }else if (gjjdkFragment.isVisible()){
                    gjjdkFragment.caculate();
                }else {
                    zhdkFragment.caculate();
                }
                break;
            case R.id.resetBtn:
                if (sydkFragment.isVisible()){
                    sydkFragment.reset();
                }else if (gjjdkFragment.isVisible()){
                    gjjdkFragment.reset();
                }else {
                    zhdkFragment.reset();
                }
                break;
        }
    }

    public void selectLL(final float baseLL, final SelectLLCallBack callBack){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                DKJS.this);
        builder.setTitle("请选择利率(基础利率:"+baseLL+")");
        //items使用全局的finalCharSequenece数组声明
        builder.setItems(showItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                String select_item = items[which].toString();
                callBack.selectItem(baseLL*Float.parseFloat(select_item));
            }
        });
        builder.show();
    }

    public interface SelectLLCallBack{
        void selectItem(float value);
    }

}
