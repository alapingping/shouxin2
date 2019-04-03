package com.shouxin.shouxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shouxin.shouxin.R;

public class SubItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvSub;


    public SubItemViewHolder(View itemView) {
        super(itemView);

        tvSub = (TextView) itemView.findViewById(R.id.word);

    }

}
