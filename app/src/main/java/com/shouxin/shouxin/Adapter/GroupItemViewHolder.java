package com.shouxin.shouxin.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouxin.shouxin.R;

public class GroupItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvGroup;
    TextView tvItemNum;
    ImageView imageView;

    public GroupItemViewHolder(View itemView) {
        super(itemView);

        tvGroup = (TextView) itemView.findViewById(R.id.word);
        tvItemNum = itemView.findViewById(R.id.subItemNum);
        imageView = itemView.findViewById(R.id.statusicon);
    }

}
