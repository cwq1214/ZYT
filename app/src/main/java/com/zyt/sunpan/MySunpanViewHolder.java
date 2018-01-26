package com.zyt.sunpan;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zyt.sunpan.activity.ReleaseOrUpdateSunpanActivity;

/**
 * Created by v7 on 2016/8/24.
 */
public class MySunpanViewHolder extends SunpanViewHolder {
    OnDelClickListener onDelClickListener;


    public MySunpanViewHolder(Context context, final OnDelClickListener onDelClickListener) {
        super(context);
        this.onDelClickListener = onDelClickListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DelOrUpdateDialog.Builder(itemView.getContext()).addItem("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReleaseOrUpdateSunpanActivity.launch(itemView.getContext(),false,sunpan);
                        dialogInterface.dismiss();
                    }
                }).addItem("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new AlertDialog.Builder(itemView.getContext()).setMessage("确定要删除吗?").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onDelClickListener.onDelClick((Integer) itemView.getTag());
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();


                        dialogInterface.dismiss();
                    }
                }).create().show();
//                ReleaseOrUpdateSunpanActivity.launch(itemView.getContext(),false,sunpan);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }


    public interface OnDelClickListener{
        void onDelClick(int position);
    }

}
