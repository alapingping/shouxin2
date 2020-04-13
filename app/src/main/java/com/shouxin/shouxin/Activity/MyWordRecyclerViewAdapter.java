package com.shouxin.shouxin.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shouxin.shouxin.Activity.WordFragment.OnListFragmentInteractionListener;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Word} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWordRecyclerViewAdapter extends RecyclerView.Adapter<MyWordRecyclerViewAdapter.ViewHolder> {

    private final List<Word> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyWordRecyclerViewAdapter(List<Word> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mWordNameView.setText(mValues.get(position).getName());
        holder.mWordDescriptionView.setText(mValues.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mWordImageView;
        public final TextView mWordNameView;
        public final TextView mWordDescriptionView;
        public Word mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mWordImageView = view.findViewById(R.id.word_image);
            mWordNameView = view.findViewById(R.id.word_name);
            mWordDescriptionView = view.findViewById(R.id.word_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mWordDescriptionView.getText() + "'";
        }
    }
}
