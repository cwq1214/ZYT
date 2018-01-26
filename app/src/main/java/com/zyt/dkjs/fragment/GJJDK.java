package com.zyt.dkjs.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zyt.HttpUtil.Bean.LoanTaxBean;
import com.zyt.R;
import com.zyt.dkjs.DKJS;
import com.zyt.dkjs.DKJSResult;
import com.zyt.util.Util;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class GJJDK extends Fragment {


    @InjectView(R.id.adkedsBtn)
    TextView adkedsBtn;
    @InjectView(R.id.amjsBtn)
    TextView amjsBtn;
    @InjectView(R.id.dkje)
    EditText dkje;
    @InjectView(R.id.dkjeLayout)
    RelativeLayout dkjeLayout;
    @InjectView(R.id.pfdj)
    EditText pfdj;
    @InjectView(R.id.pfdjLayout)
    RelativeLayout pfdjLayout;
    @InjectView(R.id.fwmj)
    EditText fwmj;
    @InjectView(R.id.fwmjLayout)
    RelativeLayout fwmjLayout;
    @InjectView(R.id.dkjx)
    EditText dkjx;
    @InjectView(R.id.dkll)
    TextView dkll;
    @InjectView(R.id.debxBtn)
    TextView debxBtn;
    @InjectView(R.id.debjBtn)
    TextView debjBtn;


    private boolean isADKED = true;
    private boolean isDEBX = true;
    private String dkllStr;


    public GJJDK() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gjjdk, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick({R.id.adkedsBtn, R.id.amjsBtn, R.id.dkll, R.id.debxBtn, R.id.debjBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adkedsBtn:
                pfdjLayout.setVisibility(View.GONE);
                fwmjLayout.setVisibility(View.GONE);
                dkjeLayout.setVisibility(View.VISIBLE);
                adkedsBtn.setBackgroundColor(Color.parseColor("#dddddd"));
                amjsBtn.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.amjsBtn:
                pfdjLayout.setVisibility(View.VISIBLE);
                fwmjLayout.setVisibility(View.VISIBLE);
                dkjeLayout.setVisibility(View.GONE);
                adkedsBtn.setBackgroundColor(Color.parseColor("#f1f1f1"));
                amjsBtn.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
            case R.id.dkll:
                if (dkjx.getText().length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(),"请先输入贷款期限",Toast.LENGTH_SHORT).show();
                }else{
                    if (!Util.isNumeric(dkjx.getText().toString())){
                        Toast.makeText(getActivity().getApplicationContext(),"贷款期限格式错误",Toast.LENGTH_SHORT).show();
                    }else {
                        LoanTaxBean bean = ((DKJS) getActivity()).getLoanTaxBean();
                        if (bean == null){
                            Toast.makeText(getActivity().getApplicationContext(),"利率获取中,请稍等",Toast.LENGTH_SHORT).show();
                        }else{
                            float lll;
                            if (Integer.parseInt(dkjx.getText().toString())<=5){
                                lll = Float.parseFloat(bean.getGjj().get("1"));
                            }else{
                                lll = Float.parseFloat(bean.getGjj().get("2"));
                            }
                            ((DKJS) getActivity()).selectLL(lll,new DKJS.SelectLLCallBack() {
                                @Override
                                public void selectItem(float value) {
                                    DecimalFormat df  = new DecimalFormat("0.00");
                                    dkllStr = df.format(value);
                                    dkll.setText(dkllStr);
                                }
                            });
                        }
                    }
                }
                break;
            case R.id.debxBtn:
                isDEBX = true;
                debxBtn.setBackgroundColor(Color.parseColor("#dddddd"));
                debjBtn.setBackgroundColor(Color.parseColor("#f1f1f1"));
                break;
            case R.id.debjBtn:
                isDEBX = false;
                debxBtn.setBackgroundColor(Color.parseColor("#f1f1f1"));
                debjBtn.setBackgroundColor(Color.parseColor("#dddddd"));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void caculate(){
        Log.e("bee","gjjdk caculate");
        if (isADKED){
            if (dkje.getText().length() == 0 || dkjx.getText().length() == 0 || dkll.getText().length() == 0){
                Toast.makeText(getActivity().getApplicationContext(),"请填写完整信息",Toast.LENGTH_SHORT).show();
            }else {
                if (!Util.isNumeric(dkje.getText().toString())){
                    Toast.makeText(getActivity().getApplicationContext(),"贷款金额格式错误",Toast.LENGTH_SHORT).show();
                }else{
                    openResultActivity();
                }
            }
        }else{
            if (pfdj.getText().length() == 0 || fwmj.getText().length() == 0 || dkjx.getText().length() == 0 || dkll.getText().length() == 0){
                Toast.makeText(getActivity().getApplicationContext(),"请填写完整信息",Toast.LENGTH_SHORT).show();
            }else {
                if (!Util.isNumeric(pfdj.getText().toString()) || !Util.isNumeric(fwmj.getText().toString())){
                    Toast.makeText(getActivity().getApplicationContext(),"信息格式错误",Toast.LENGTH_SHORT).show();
                }else {
                    openResultActivity();
                }
            }
        }
    }

    public void reset(){
        Log.e("bee","gjjdk reset");
        dkje.setText("");
        dkjx.setText("");
        dkll.setText("");
        pfdj.setText("");
        fwmj.setText("");
    }

    private void openResultActivity(){
        Bundle b = new Bundle();

        if (isADKED){
            b.putInt("dkje",Integer.parseInt(dkje.getText().toString()));
        }else{
            b.putInt("dkje",Integer.parseInt(pfdj.getText().toString())*Integer.parseInt(fwmj.getText().toString()));
        }

        b.putBoolean("isDEBX",isDEBX);
        b.putInt("dkqx",Integer.parseInt(dkjx.getText().toString()));
        b.putString("dkll",dkllStr);

        Intent i = new Intent();
        i.setClass((DKJS)getActivity(), DKJSResult.class);
        i.putExtra("data",b);
        getActivity().startActivity(i);
    }
}
