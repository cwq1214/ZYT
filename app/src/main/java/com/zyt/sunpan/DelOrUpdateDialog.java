package com.zyt.sunpan;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyt.R;
import com.zyt.util.DensityUtils;
import com.zyt.util.ScreenUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/8/24.
 */
public class DelOrUpdateDialog extends Dialog {

    Context mContext;



    public DelOrUpdateDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static class Builder implements View.OnClickListener {

        Context mContext;
        LinearLayout layout;

        ArrayList<String> strings = new ArrayList<>();
        ArrayList<OnClickListener> onClickListeners = new ArrayList<>();

        DelOrUpdateDialog delOrUpdateDialog;
        public Builder(Context context){
            mContext = context;
        }

        public Builder addItem(String text,OnClickListener listener){
            strings.add(text);
            onClickListeners.add(listener);
            return this;
        }

        public DelOrUpdateDialog create(){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            delOrUpdateDialog = new DelOrUpdateDialog(mContext);
            View rootView = inflater.inflate(R.layout.layout_del_or_update_dialog,null);
            delOrUpdateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            delOrUpdateDialog.addContentView(rootView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout = (LinearLayout) rootView.findViewById(R.id.layout);
            if (strings!=null||strings.size()!=0){
                for (int i = 0 ; i<strings.size() ; i++){
                    TextView textView = new TextView(mContext);
                    textView.setLayoutParams(new LinearLayout.LayoutParams((int) (ScreenUtils.getScreenWidth(mContext)*0.6), DensityUtils.dp2px(mContext,48)));
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(strings.get(i));
                    textView.setOnClickListener(this);
                    textView.setBackgroundColor(Color.parseColor("#ffffff"));
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setTag(i);
                    layout.addView(textView);
                }
            }

            delOrUpdateDialog.setContentView(rootView);
            return delOrUpdateDialog;
        }

        @Override
        public void onClick(View view) {
            onClickListeners.get((Integer) view.getTag()).onClick(delOrUpdateDialog,0);
        }
    }
}
