package com.shouxin.shouxin.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.shouxin.shouxin.Adapter.CommunityAdapter;
import com.shouxin.shouxin.Adapter.SampleAdapter;
import com.shouxin.shouxin.DataModel.SampleData;
import com.shouxin.shouxin.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener, ScreenShotable {

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private SampleAdapter mAdapter;

    private CommunityAdapter mcAdapter;
    private RecyclerView recyclerView;


    private PullToRefreshView mPullToRefreshView;

    private static CommunityFragment communityFragment = new CommunityFragment();

    private ArrayList<String> mData;

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView = view.findViewById(R.id.recycler);
//        mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 500);
            }
        });



        if (savedInstanceState == null) {
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
            View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
            TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
            TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
            txtHeaderTitle.setText("下拉刷新");
            txtFooterTitle.setText("没有更多内容啦!");


            mData = new ArrayList<>();
            mData.add("1");
            mData.add("2");
            mData.add("3");
            mcAdapter = new CommunityAdapter(mData, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            mGridView.addHeaderView(header);
//            mGridView.addFooterView(footer);
        } else {
            mData = savedInstanceState.getStringArrayList("data");
        }

//        if (mAdapter == null) {
//            mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);
//        }
//
//        if (mData == null) {
//            mData = SampleData.generateSampleData();
//
//        }
//
//        for (String data : mData) {
//            mAdapter.add(data);
//        }

//        mGridView.setAdapter(mAdapter);
//        mGridView.setOnScrollListener(this);
//        mGridView.setOnItemClickListener(this);
        recyclerView.setAdapter(mcAdapter);
        return view;
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        final ArrayList<String> sampleData = SampleData.generateSampleData();
        for (String data : sampleData) {
            mAdapter.add(data);
        }
        // stash all the data in our backing store
        mData.addAll(sampleData);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    public static CommunityFragment getInstance(){
        if(communityFragment == null){
            communityFragment = new CommunityFragment();
        }
        return communityFragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("data", mData);
    }

}
