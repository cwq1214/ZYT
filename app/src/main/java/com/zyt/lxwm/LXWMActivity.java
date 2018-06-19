package com.zyt.lxwm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyt.HttpUtil.Bean.AboutUs;
import com.zyt.R;
import com.zyt.base.BaseJSON;
import com.zyt.util.ConstList;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

public class LXWMActivity extends AppCompatActivity {

    @InjectView(R.id.textView64)
    TextView textView64;
    @InjectView(R.id.relativeLayout10)
    RelativeLayout relativeLayout10;
    @InjectView(R.id.androidImg)
    ImageView androidImg;
    @InjectView(R.id.androidText)
    TextView androidText;
    @InjectView(R.id.iosImg)
    ImageView iosImg;
    @InjectView(R.id.iosText)
    TextView iosText;
    @InjectView(R.id.address)
    TextView address;
    @InjectView(R.id.tellayout)
    LinearLayout tellayout;
    @InjectView(R.id.copyright)
    TextView copyright;
    @InjectView(R.id.tel)
    TextView tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lxwm);
        ButterKnife.inject(this);

        getRemoteData();

        tel.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        tel.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @OnClick({R.id.backBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn:
                finish();
                break;

        }
    }

    private void getRemoteData() {
        //{"forUser": "", "forWorker": "", "version": "1.0", "data": {"iosImg": "http://120.76.233.32/iosImg.jpg", "androidImg": "http://120.76.233.32/androidImg.jpg"}, "ret": "success", "code": 200}
        OkHttpUtils.get().url(ConstList.domain + "/server/about/").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                BaseJSON<AboutUs> json = new Gson().fromJson(response, new TypeToken<BaseJSON<AboutUs>>() {
                }.getType());
                if (json.getRet().equals("success")) {
                    Glide.with(LXWMActivity.this).load(json.getData().getAndroidImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(androidImg);
                    Glide.with(LXWMActivity.this).load(json.getData().getIosImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(iosImg);
                }
            }
        });
    }
}
