package com.shouxin.shouxin.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.Activity.EditMessageActivity;
import com.shouxin.shouxin.Adapter.CommunityAdapter;
import com.shouxin.shouxin.DataModel.Message;
import com.shouxin.shouxin.databinding.FragmentCommunityBinding;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment implements
        AbsListView.OnScrollListener, AbsListView.OnItemClickListener, ScreenShotable {

    private FragmentCommunityBinding communityBinding;

    private boolean mHasRequestedMore;

    private CommunityAdapter mAdapter;
    // 用户的动态
    private ArrayList<Message> messages;
    // 判断数据是否变动过
    private String lastMessages;
    // 下拉刷新控件
    private PullToRefreshView mPullToRefreshView;

    private static volatile CommunityFragment communityFragment;

    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        communityBinding = FragmentCommunityBinding.inflate(inflater,  container, false);
        communityBinding.shareButton.setOnClickListener(
                v -> {
                    // 启动编辑动态界面
                    Intent intent = new Intent(getActivity(), EditMessageActivity.class);
                    startActivityForResult(intent, 1);
                }
        );
        mPullToRefreshView = communityBinding.pullToRefresh;
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 向服务器获取数据
                        getAllMessages();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 500);
            }
        });

        if (savedInstanceState == null) {
//            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//            View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//            View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
//            TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
//            TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
//            txtHeaderTitle.setText("下拉刷新");
//            txtFooterTitle.setText("没有更多内容啦!");

            messages = new ArrayList<>();
            lastMessages = null;
            getAllMessages();
            mAdapter = new CommunityAdapter(messages, getActivity());
            communityBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            messages = savedInstanceState.getParcelableArrayList("data");
            lastMessages = savedInstanceState.getString("lastMessages");
        }

//
//        for (String data : mData) {
//            mAdapter.add(data);
//        }

//        mGridView.setAdapter(mAdapter);
//        mGridView.setOnScrollListener(this);
//        mGridView.setOnItemClickListener(this);
        communityBinding.recycler.setAdapter(mAdapter);
        return communityBinding.getRoot();
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
//                onLoadMoreItems();
            }
        }
    }

//    private void onLoadMoreItems() {
//        final ArrayList<String> sampleData = SampleData.generateSampleData();
//        for (String data : sampleData) {
//            mAdapter.add(data);
//        }
//        // stash all the data in our backing store
//        mData.addAll(sampleData);
//        // notify the adapter that we can update now
//        mAdapter.notifyDataSetChanged();
//        mHasRequestedMore = false;
//    }

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
            synchronized (CommunityFragment.class) {
                if (communityFragment == null) {
                    communityFragment = new CommunityFragment();
                }
            }
        }
        return communityFragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data", messages);
        outState.putString("lastMessages", lastMessages);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("CommunityFragment-----", "onHiddenChanged");
    }


    public void getAllMessages() {
        Service service = Client.retrofit.create(Service.class);
        Call<ResponseBody> call = service.getAllMessages();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    if (code == 200) {
                        JSONArray array = object.getJSONArray("datas");
                        if (!array.toString().equals(lastMessages)) {
                            lastMessages = array.toString();
                            Array2Messages(array);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT);
                        }
                    } else {
                        Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("1", "1");
            }

        });
    }

    private void Array2Messages(JSONArray array) throws JSONException {
        messages.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String username = object.getString("username");
            String content = object.getString("content");
            String time = object.getString("time");
            Message message = new Message(username, content, time);
            messages.add(message);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (resultCode) {
            // 分享成功
            case 1:
                Message message = data.getParcelableExtra("newMessage");
                messages.add(0, message);
                mAdapter.notifyDataSetChanged();
                break;
             // 分享失败
            case -1:
                Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }

    }


}
