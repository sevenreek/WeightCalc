package com.devseven.gympack.materialsetlogger.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.devseven.gympack.materialsetlogger.R;


/**
 * Created by Dickbutt on 10.02.2018.
 */

public class SimpleDividerLine extends RecyclerView.ItemDecoration {
    private Drawable divider;
    int pixel;
    public SimpleDividerLine(Context context, int paddingInDP)
    {
        pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,paddingInDP,context.getResources().getDisplayMetrics());
        divider = ContextCompat.getDrawable(context, R.drawable.simple_line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft()+pixel;
        int right = parent.getWidth() - parent.getPaddingRight()-pixel;
        int childCount = parent.getChildCount();
        for(int i =0; i<childCount; i++)
        {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left,top,right,bottom);
            divider.draw(c);
        }
    }
}
