package com.shouxin.shouxin.Adapter.ItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.shouxin.shouxin.Adapter.ItemDecoration.RvDividerItemDecoration;

import androidx.recyclerview.widget.RecyclerView;

public class CommunityDividerItemDecoration extends BaseItemDecoration {

    public CommunityDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    //获取Item的位置，然后为每个item定位画线
    @Override
    protected void drawForVertical(Canvas c, RecyclerView parent) {
        final int left = 0;
        final int right = parent.getWidth();

        final int childCount = parent.getChildCount();
        for ( int i = 0; i < childCount - 1; i++ ) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + 20;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

}
