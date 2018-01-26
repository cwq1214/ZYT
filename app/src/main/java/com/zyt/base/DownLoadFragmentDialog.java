package com.zyt.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zyt.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by v7 on 2016/8/10.
 */
public class DownLoadFragmentDialog extends DialogFragment {


    View rootView;
    @InjectView(R.id.msg)
    TextView msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (rootView == null)
            rootView = inflater.inflate(R.layout.fragment_download_dialog, null);
        ButterKnife.inject(this, rootView);
        return rootView;

    }

    public void setText(final String text) {
        if (msg!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msg.setText(text);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            setCancelable(false);
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
