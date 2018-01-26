package com.zyt.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by v7 on 2016/7/27.
 */
public class ColorDecoration extends RecyclerView.ItemDecoration {

    int color;
    int width;
    int orientation;

    Paint mPaint = new Paint();

    public ColorDecoration(int color, int width, int orientation) {
        this.color = color;
        this.width = width;
        this.orientation = orientation;
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if(orientation == LinearLayoutManager.VERTICAL){
            drawVertical(c,parent) ;
        }else {
            drawHorizontal(c,parent) ;
        }
    }
    /**
     * 绘制纵向 item 分割线
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas,RecyclerView parent){
        final int left = parent.getPaddingLeft() ;
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight() ;
        final int childSize = parent.getChildCount() ;
        for(int i = 0 ; i < childSize ; i ++){
            final View child = parent.getChildAt( i ) ;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin ;
            final int bottom = top + width ;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }

    /**
     * 绘制横向 item 分割线
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas,RecyclerView parent){
        final int top = parent.getPaddingTop() ;
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() ;
        final int childSize = parent.getChildCount() ;
        for(int i = 0 ; i < childSize ; i ++){
            final View child = parent.getChildAt( i ) ;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin ;
            final int right = left + width ;
            canvas.drawRect(left,top,right,bottom,mPaint);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(orientation == LinearLayoutManager.VERTICAL){
            outRect.set(0,0,0,width);
        }else {
            outRect.set(0,0,width,0);
        }
    }
}
