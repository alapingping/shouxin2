package com.shouxin.shouxin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shouxin.shouxin.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    private ArrayList<String> mdata;

    public MenuAdapter(ArrayList<String> list) {
        this.mdata = list;
    }


    public static class VH extends RecyclerView.ViewHolder {
        public final TextView textView;
        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
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
        holder.textView.setText(mdata.get(position));
    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
