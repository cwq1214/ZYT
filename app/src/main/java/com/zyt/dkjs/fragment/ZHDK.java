package com.zyt.dkjs.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.HttpUtil.Bean.LoanTaxBean;
import com.zyt.R;
import com.zyt.dkjs.DKJSActivity;
import com.zyt.dkjs.DKJSResultActivity;
import com.zyt.util.Util;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZHDK extends Fragment {


    @InjectView(R.id.sydkje)
    EditText sydkje;
    @InjectView(R.id.sydkll)
    TextView sydkll;
    @InjectView(R.id.gjjdkje)
    EditText gjjdkje;
    @InjectView(R.id.gjjdkll)
    TextView gjjdkll;
    @InjectView(R.id.dkqx)
    EditText dkqx;
    @InjectView(R.id.debxBtn)
    TextView debxBtn;
    @InjectView(R.id.debjBtn)
    TextView debjBtn;

    private String sydkllStr;
    private String gjjdkllStr;

    private boolean isDEBX = true;

    public ZHDK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhdk, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public void caculate() {
        Log.e("bee", "zhdk caculate");
        if (sydkje.getText().length() == 0 || sydkll.getText().length() == 0 || gjjdkje.getText().length() == 0 || gjjdkll.getText().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(),"请填写完整信息",Toast.LENGTH_SHORT).show();
        }else{
            openResultActivity();
        }
    }

    private void openResultActivity(){
        Bundle b = new Bundle();

        b.putBoolean("isDEBX",isDEBX);
        b.putBoolean("isZH",true);
        b.putInt("dkqx",Integer.parseInt(dkqx.getText().toString()));
        b.putString("sydkll",sydkllStr);
        b.putString("gjjdkll",gjjdkllStr);

        b.putInt("sydkje",Integer.parseInt(sydkje.getText().toString()));
        b.putInt("gjjdkje",Integer.parseInt(gjjdkje.getText().toString()));

        Intent i = new Intent();
        i.setClass((DKJSActivity)getActivity(), DKJSResultActivity.class);
        i.putExtra("data",b);
        getActivity().startActivity(i);
    }

    public void reset() {
        Log.e("bee", "zhdk reset");
        sydkje.setText("");
        sydkll.setText("");
        gjjdkje.setText("");
        gjjdkll.setText("");
        dkqx.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.sydkll, R.id.gjjdkll, R.id.debxBtn, R.id.debjBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sydkll:
                if (dkqx.getText().length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"请先输入贷款期限",Toast.LENGTH_SHORT).show();
                }else{
                    if (!Util.isNumeric(dkqx.getText().toString())){
                        Toast.makeText(getActivity().getApplicationContext(),"贷款期限格式错误",Toast.LENGTH_SHORT).show();
                    }else {
                        LoanTaxBean bean = ((DKJSActivity) getActivity()).getLoanTaxBean();
                        if (bean == null){
                            Toast.makeText(getActivity().getApplicationContext(),"利率获取中,请稍等",Toast.LENGTH_SHORT).show();
                        }else{
                            float lll;
                            if (Integer.parseInt(dkqx.getText().toString())<=1){
                                lll = Float.parseFloat(bean.getBusiness().get("1"));
                            }else if (Integer.parseInt(dkqx.getText().toString())<=5){
                                lll = Float.parseFloat(bean.getBusiness().get("2"));
                            }else{
                                lll = Float.parseFloat(bean.getBusiness().get("3"));
                            }
                            ((DKJSActivity) getActivity()).selectLL(lll,new DKJSActivity.SelectLLCallBack() {
                                @Override
                                public void selectItem(float value) {
                                    DecimalFormat df  = new DecimalFormat("0.00");
                                    sydkllStr = df.format(value);
                                    sydkll.setText(sydkllStr);
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.gjjdkll:
                if (dkqx.getText().length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"请先输入贷款期限",Toast.LENGTH_SHORT).show();
                }else{
                    if (!Util.isNumeric(dkqx.getText().toString())){
                        Toast.makeText(getActivity().getApplicationContext(),"贷款期限格式错误",Toast.LENGTH_SHORT).show();
                    }else {
                        LoanTaxBean bean = ((DKJSActivity) getActivity()).getLoanTaxBean();
                        if (bean == null){
                            Toast.makeText(getActivity().getApplicationContext(),"利率获取中,请稍等",Toast.LENGTH_SHORT).show();
                        }else{
                            float lll;
                            if (Integer.parseInt(dkqx.getText().toString())<=5){
                                lll = Float.parseFloat(bean.getGjj().get("1"));
                            }else{
                                lll = Float.parseFloat(bean.getGjj().get("2"));
                            }
                            ((DKJSActivity) getActivity()).selectLL(lll,new DKJSActivity.SelectLLCallBack() {
                                @Override
                                public void selectItem(float value) {
                                    DecimalFormat df  = new DecimalFormat("0.00");
                                    gjjdkllStr = df.format(value);
                                    gjjdkll.setText(gjjdkllStr+"%");
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.debxBtn:
                isDEBX = true;

                debxBtn.setBackgroundResource(R.drawable.btn_radio_sel);
                debxBtn.setTextColor(Color.WHITE);

                debjBtn.setBackgroundResource(0);
                debjBtn.setTextColor(Color.BLACK);


                break;
            case R.id.debjBtn:
                isDEBX = false;

                debjBtn.setBackgroundResource(R.drawable.btn_radio_sel);
                debjBtn.setTextColor(Color.WHITE);

                debxBtn.setBackgroundResource(0);
                debxBtn.setTextColor(Color.BLACK);
                break;
        }
    }
}
