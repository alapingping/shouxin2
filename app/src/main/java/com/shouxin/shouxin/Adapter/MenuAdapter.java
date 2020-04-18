package com.shouxin.shouxin.Adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    private ArrayMap<String, Integer> mdata;
    private Context context;

    public MenuAdapter(Context context, ArrayMap<String, Integer> list) {
        this.mdata = list;
        this.context = context;
    }


     static class VH extends RecyclerView.ViewHolder {
         final TextView item_name;
         final ImageView item_icon;
         VH(@NonNull View itemView) {
            super(itemView);
             item_name = itemView.findViewById(R.id.item_name);
             item_icon = itemView.findViewById(R.id.item_icon);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String key = mdata.keyAt(position);
        holder.item_name.setText(key);
        holder.item_icon.setImageDrawable(context.getDrawable(mdata.get(key)));
    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
