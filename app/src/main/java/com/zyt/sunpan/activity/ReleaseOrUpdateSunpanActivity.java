package com.zyt.sunpan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zyt.HttpUtil.Bean.ServerBean;
import com.zyt.HttpUtil.BeanCallBack;
import com.zyt.R;
import com.zyt.sunpan.bean.Sunpan;
import com.zyt.util.ConstList;
import com.zyt.util.Util;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by chenbifeng on 16/7/30.
 */
public class ReleaseOrUpdateSunpanActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();

    @InjectView(R.id.relativeLayout10)
    RelativeLayout relativeLayout10;
    @InjectView(R.id.input_apartmentName)
    EditText inputApartmentName;
    @InjectView(R.id.input_buildingNo)
    EditText inputBuildingNo;
    @InjectView(R.id.intput_floor)
    EditText inputFloor;
    @InjectView(R.id.input_area)
    EditText inputArea;
    @InjectView(R.id.input_price)
    EditText inputPrice;
    @InjectView(R.id.input_contart)
    EditText inputContart;
    @InjectView(R.id.input_tel)
    EditText inputTel;
    @InjectView(R.id.input_companyName)
    EditText inputCompanyName;
    @InjectView(R.id.submit)
    TextView submit;
    @InjectView(R.id.apartmentName)
    TextView apartmentName;
    @InjectView(R.id.tel)
    TextView tel;
    @InjectView(R.id.price)
    TextView price;



    boolean isSubmiting = false;
    boolean isComplete = false;


    static boolean isRelease;
    static Sunpan sunpan;
    public static void launch(Context context, boolean isRelease, Sunpan sunpan){
        context.startActivity(new Intent(context,ReleaseOrUpdateSunpanActivity.class));
        ReleaseOrUpdateSunpanActivity.isRelease = isRelease;
        ReleaseOrUpdateSunpanActivity.sunpan = sunpan;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_sunpan);

        ButterKnife.inject(this);

        apartmentName.setText(Html.fromHtml("<font color='red'>*</font>楼盘名称"));
        tel.setText(Html.fromHtml("<font color='red'>*</font>联系电话"));
        price.setText(Html.fromHtml("<font color='red'>*</font>平方单价"));

        if (sunpan!=null){
            inputApartmentName.setText(sunpan.getSpName());
            inputArea.setText(sunpan.getSpArea());
            inputBuildingNo.setText(sunpan.getSpBuildingNo());
            inputCompanyName.setText(sunpan.getSpCompanyName());
            inputContart.setText(sunpan.getSpUsername());
            inputFloor.setText(sunpan.getSpFloor());
            inputTel.setText(sunpan.getSpPhone());
            inputPrice.setText(sunpan.getSpPrice());
            inputTel.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sunpan = null;
    }

    @OnClick(R.id.submit)
    public void onSubmitClick(){

        if (isRelease){
            submitRelease();
        }else{
            submitUpdate();
        }

    }


    private void submitRelease(){
        if(TextUtils.isEmpty(inputApartmentName.getText().toString())){
            makeToast("请填写楼盘名称");
            return;
        }
        if (TextUtils.isEmpty(inputPrice.getText().toString())){
            makeToast("请填写平方价格");
            return;
        }
        if (TextUtils.isEmpty(inputTel.getText().toString())){
            makeToast("请填写联系电话");
            return;
        }
//        if(isSubmiting){
//            makeToast("");
//            return;
//        }
        if (isComplete){
            finish();
            return;
        }

        setEditable(false);

        submit.setEnabled(false);

        OkHttpUtils.post().url(getString(R.string.baseUrl)+"/server/sunpan/add")
                .addParams("spName",inputApartmentName.getText().toString())
                .addParams("spPrice",inputPrice.getText().toString())
                .addParams("spFloor",inputFloor.getText().toString())
                .addParams("spArea",inputArea.getText().toString())
                .addParams("spUsername",inputContart.getText().toString())
                .addParams("spPhone",inputTel.getText().toString())
                .addParams("spBuildingNo",inputBuildingNo.getText().toString())
                .addParams("spCompanyName",inputCompanyName.getText().toString())
                .addParams("userId", Util.getUserID(ReleaseOrUpdateSunpanActivity.this))
                .build().execute(new BeanCallBack<ServerBean<Object>>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.setEnabled(true);
                setEditable(true);
                isComplete = false;
            }

            @Override
            public void onResponse(ServerBean<Object> response, int id) {
                submit.setEnabled(true);
                setEditable(true);

                Log.e(TAG,response.toString());

                if (response.getRet().equalsIgnoreCase("success")){
                    Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_SHORT).show();
                    submit.setText("完成");
                    isComplete = true;
                    finish();
                }
            }
        });
    }

    private void submitUpdate(){
        if(TextUtils.isEmpty(inputApartmentName.getText().toString())){
            makeToast("请填写楼盘名称");
            return;
        }
        if (TextUtils.isEmpty(inputPrice.getText().toString())){
            makeToast("请填写平方价格");
            return;
        }
        if (isComplete){
            finish();
            return;
        }
        OkHttpUtils.post().url(getString(R.string.baseUrl)+"/server/sunpan/update")
                .addParams("spName",inputApartmentName.getText().toString())
                .addParams("spPrice",inputPrice.getText().toString())
                .addParams("spFloor",inputFloor.getText().toString())
                .addParams("spArea",inputArea.getText().toString())
                .addParams("spUsername",inputContart.getText().toString())
                .addParams("spPhone",inputTel.getText().toString())
                .addParams("spBuildingNo",inputBuildingNo.getText().toString())
                .addParams("spCompanyName",inputCompanyName.getText().toString())
                .addParams("userId", Util.getUserID(ReleaseOrUpdateSunpanActivity.this))
                .addParams("sunPanId",sunpan.getSunPanId())
                .build().execute(new BeanCallBack<ServerBean<Object>>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                submit.setEnabled(true);
                setEditable(true);
                isComplete = false;
            }

            @Override
            public void onResponse(ServerBean<Object> response, int id) {
                submit.setEnabled(true);
                setEditable(true);

                Log.e(TAG,response.toString());

                if (response.getRet().equalsIgnoreCase("success")){
                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    submit.setText("完成");
                    isComplete = true;
                    finish();
                }
            }
        });
    }


    public void setEditable(boolean enable){
        inputApartmentName.setEnabled(enable);
        inputArea.setEnabled(enable);
        inputBuildingNo.setEnabled(enable);
        inputCompanyName.setEnabled(enable);
        inputContart.setEnabled(enable);
        inputTel.setEnabled(enable);
        inputPrice.setEnabled(enable);
        inputFloor.setEnabled(enable);


    }

    @OnClick(R.id.backBtn)
    public void onBackBtnClick(){
        finish();
    }

    public void makeToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
