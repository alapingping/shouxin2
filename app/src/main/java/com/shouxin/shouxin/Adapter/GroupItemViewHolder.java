package com.shouxin.shouxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shouxin.shouxin.R;

public class GroupItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvGroup;


    public GroupItemViewHolder(View itemView) {
        super(itemView);

        tvGroup = (TextView) itemView.findViewById(R.id.word);

    }

}
