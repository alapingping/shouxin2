package com.shouxin.shouxin.Adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.Activity.BottomNavigationActivity;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.fragment.CommunityFragment;
import com.shouxin.shouxin.fragment.DictionaryFragment;
import com.shouxin.shouxin.fragment.ModeChoiceFragment;
import com.shouxin.shouxin.fragment.PersonalFragment;
import com.shouxin.shouxin.fragment.WordFragment;

import java.util.ArrayList;
import java.util.List;

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
         final LinearLayout layout;
         VH(@NonNull View itemView) {
            super(itemView);
             item_name = itemView.findViewById(R.id.item_name);
             item_icon = itemView.findViewById(R.id.item_icon);
             layout = itemView.findViewById(R.id.menu_item_layout);
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
        if (key.equals("收藏")) {
            holder.layout.setOnClickListener(v -> {
                BottomNavigationActivity activity;
                activity = (BottomNavigationActivity) context;
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(CommunityFragment.getInstance())
                        .hide(ModeChoiceFragment.getInstance())
                        .hide(PersonalFragment.getInstance())
                        .hide(DictionaryFragment.getInstance())
                        .add(R.id.container_frame, WordFragment.newInstance(getFavoriteWords()))
                        .addToBackStack("favorite")
                        .show(WordFragment.newInstance(1));
                ft.commit();
            });
        }

    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    private List<Word> getFavoriteWords() {
        List<Word> words = new ArrayList<>();
        for (Word word:DictionaryFragment.getWords()) {
            if (word.getCollected() == 1) {
                words.add(word);
            }
        }
        return words;
    }
}
