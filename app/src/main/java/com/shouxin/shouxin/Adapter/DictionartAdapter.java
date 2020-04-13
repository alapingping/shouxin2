package com.shouxin.shouxin.Adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DictionartAdapter extends RecyclerView.Adapter<DictionartAdapter.CategoryViewHolder> {

    private ArrayMap<String, List<Word>> categories;
    private Context context;

    public DictionartAdapter(Context context, ArrayMap<String, List<Word>> datas) {
        this.categories = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dictionary_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        String key = categories.keyAt(position);
        int nums = categories.get(key).size();
        holder.category.setText(key);
        holder.numOfWors.setText(String.valueOf(nums));
        holder.layout.setOnClickListener( v -> {

        });

    }

    @Override
    public int getItemCount() {
        if (categories != null) {
            return categories.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView category;
        TextView numOfWors;
        ConstraintLayout layout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
            numOfWors = itemView.findViewById(R.id.numOfWords);
            layout = itemView.findViewById(R.id.item_category);
        }
    }

    public void setData(ArrayMap<String, List<Word>> datas) {
        this.categories = datas;
        notifyDataSetChanged();
    }

}
