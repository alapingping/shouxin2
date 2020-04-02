package com.shouxin.shouxin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Activity.SingleWordActivity;

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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);

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
        ((GroupItemViewHolder) holder).tvItemNum.setText(String.valueOf(dts.get(groupItemIndex).getSubItemsNum()));
    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

        ((SubItemViewHolder) holder).tvSub.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).getName());

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

        if(!isExpand){
            holder.imageView.setBackground(context.getDrawable(R.drawable.ic_expend));
        }else{
            holder.imageView.setBackground(context.getDrawable(R.drawable.ic_fold));
        }

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

        ItemEntry word = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        Bundle bundle = new Bundle();
        bundle.putSerializable("word", word);

        Intent intent = new Intent(context, SingleWordActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
