package com.shouxin.shouxin.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shouxin.shouxin.Adapter.MenuAdapter;
import com.shouxin.shouxin.Adapter.RvDividerItemDecoration;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.SPHelper;
import com.shouxin.shouxin.databinding.FragmentPersonalBinding;

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    private ArrayMap<String, Integer> mdata;
    private RecyclerView recyclerView;
    private static volatile PersonalFragment fragment;
    private FragmentPersonalBinding binding;

    public PersonalFragment() {
        // Required empty public constructor
        mdata = new ArrayMap<>();
        final String item1 = "收藏";
        final String item2 = "历史记录";
        final String item3 = "版本更新";
        final String item4 = "关于";
        mdata.put(item1, R.drawable.ic_person_page_favorite);
        mdata.put(item2, R.drawable.ic_person_page_history);
        mdata.put(item3, R.drawable.ic_person_page_version);
        mdata.put(item4, R.drawable.ic_person_page_about);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PersonalFragment.
     */
    public static PersonalFragment getInstance() {
        if (fragment == null) {
            synchronized (PersonalFragment.class) {
                if (fragment == null) {
                    fragment = new PersonalFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        Glide.with(this)
                .load(R.drawable.dummy_head_shot)
                .into(binding.PersonHeadShot);
        binding.username.setText(SPHelper.getUsername(getContext()));
        recyclerView = binding.menu;
        recyclerView.setAdapter(new MenuAdapter(getActivity(), this.mdata));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RvDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        return binding.getRoot();
    }


}
