package com.shouxin.shouxin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends SecondaryListAdapter<GroupItemViewHolder, SubItemViewHolder>{

    private Context context;

    private List<DataTree<String, ItemEntry>> dts = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_word, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_word, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {

        ((GroupItemViewHolder) holder).tvGroup.setText(dts.get(groupItemIndex).getGroupItem());

    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

        ((SubItemViewHolder) holder).tvSub.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).getName());

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

        Toast.makeText(context, "group item " + String.valueOf(groupItemIndex) + " is expand " +
                String.valueOf(isExpand), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

        Toast.makeText(context, "sub item " + String.valueOf(subItemIndex) + " in group item " +
                String.valueOf(groupItemIndex), Toast.LENGTH_SHORT).show();

        

    }
}
