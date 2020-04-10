package com.shouxin.shouxin.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.Adapter.RecyclerAdapter;
import com.shouxin.shouxin.Adapter.RvDividerItemDecoration;
import com.shouxin.shouxin.Adapter.SecondaryListAdapter;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.databinding.FragmentDictionaryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DictionaryFragment extends Fragment {

    private static volatile DictionaryFragment fragment;
    private FragmentDictionaryBinding binding;
    private List<SecondaryListAdapter.DataTree<String, Word>> datas;
    private List<Word> words;
    private RecyclerAdapter mAdapter;

    public static DictionaryFragment getInstance(){
        if (fragment == null) {
            synchronized (DictionaryFragment.class) {
                if (fragment == null) {
                    fragment = new DictionaryFragment();
                }
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        datas = new ArrayList<>();
        getAllWords();
        RecyclerView rv = binding.recycler;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new RvDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mAdapter = new RecyclerAdapter(getActivity());
        mAdapter.setData(datas);
        rv.setAdapter(mAdapter);

        return binding.getRoot();
    }

    private void getAllWords() {

        Service service = Client.retrofit.create(Service.class);
        Call<ResponseBody> call = service.getAllWords();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    if (code == 200) {
                        Array2Words(object.getJSONArray("datas"));
                        mAdapter.notifyNewData(datas);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

//        List<Word> items = new ArrayList<Word>(){{
//            add(new Word("a","一个苹果", "图片路径1"));
//            add(new Word("b","一个鸭梨", "图片路径2"));
//            add(new Word("c","一个榴莲", "图片路径3"));
//        }};
//        List<String> groups = new ArrayList<String>(){{
//            add("字母");add("称谓");add("职业");add("姓氏");add("衣物");
//            add("食品");add("社交");add("时间");add("空间");add("生活");}};
//
//        for (String groupName:groups) {
//
//            datas.add(new SecondaryListAdapter.DataTree<String, Word>(groupName,
//                    items)
//            );
//        }

    }

    private void Array2Words(JSONArray array) throws JSONException {
        if (array.length() == 0) {return ;}
        List<Word> words = new ArrayList<>();
        String curCategory = array.getJSONObject(0).getString("category");
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String category = object.getString("category");
            if (!curCategory.equals(category)) {
                datas.add(new SecondaryListAdapter.DataTree<String, Word>(curCategory, words));
                words = new ArrayList<>();
                curCategory = category;
            }
            words.add(new Word(object.getString("name"),
                    object.getString("description"),
                    object.getString("pictureURL")));
        }
        datas.add(new SecondaryListAdapter.DataTree<String, Word>(curCategory, words));
    }


}
