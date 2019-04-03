package com.shouxin.shouxin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouxin.shouxin.Adapter.RecyclerAdapter;
import com.shouxin.shouxin.Adapter.RvDividerItemDecoration;
import com.shouxin.shouxin.Adapter.SecondaryListAdapter;
import com.shouxin.shouxin.DataModel.ItemEntry;
import com.shouxin.shouxin.R;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DictionaryFragment extends Fragment {

    private static DictionaryFragment dictionaryFragment = new DictionaryFragment();

    private List<SecondaryListAdapter.DataTree<String, ItemEntry>> datas = new ArrayList<>();

    {

        List<ItemEntry> items = new ArrayList<ItemEntry>(){{
            add(new ItemEntry("a","一个苹果", "图片路径1"));
            add(new ItemEntry("b","一个鸭梨", "图片路径2"));
            add(new ItemEntry("c","一个榴莲", "图片路径3"));
        }};
        List<String> groups = new ArrayList<String>(){{
            add("字母");add("称谓");add("职业");add("姓氏");add("衣物");
            add("食品");add("社交");add("时间");add("空间");add("生活");}};

        for (String groupName:groups) {

            datas.add(new SecondaryListAdapter.DataTree<String, ItemEntry>(groupName,
                    items)
            );

        }

    }


    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment getInstance(){
        if(dictionaryFragment == null){
            dictionaryFragment = new DictionaryFragment();
        }
        return dictionaryFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new RvDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        RecyclerAdapter ra = new RecyclerAdapter(getContext());
        ra.setData(datas);
        rv.setAdapter(ra);

        return view;
    }

}
