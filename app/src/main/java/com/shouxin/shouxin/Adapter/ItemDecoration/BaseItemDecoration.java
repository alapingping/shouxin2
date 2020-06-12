package com.shouxin.shouxin.Adapter.ItemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseItemDecoration  extends RecyclerView.ItemDecoration{

    public final String TAG = this.getClass().getSimpleName();

    private static final int[] ATTRS = new int[] {
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    protected Drawable divider;

    protected int orientation;


    public BaseItemDecoration(Context context, int orientation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int pOrientation) {
        if (pOrientation != HORIZONTAL_LIST && pOrientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        orientation = pOrientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            drawForVertical(c, parent);
        } else {
            drawForHorizontal(c, parent);
        }
    }

    //获取Item的位置，然后为每个item定位画线
    protected abstract void drawForVertical(Canvas c, RecyclerView parent);

    protected void drawForHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for ( int i = 0; i < childCount; i++ ) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getRight() + params.rightMargin;
            final int right = left + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    //可以通过outRect()为每个Item设置一定的偏移量。
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        }
    }

}
