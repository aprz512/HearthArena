package com.aprz.heartharena.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aprz.heartharena.R;
import com.aprz.heartharena.app.App;

/**
 * Created by aprz on 17-8-6.
 *
 * -- 卡片间隔
 */

public class CardItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public CardItemDecoration() {
        this.space = (int) App.getInstance().getResources().getDimension(R.dimen.card_item_spacing);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.top = space;
            outRect.bottom = 0;
        } else if (position == parent.getAdapter().getItemCount() - 1){
            outRect.top = 0;
            outRect.bottom = space;
        }
    }

}
