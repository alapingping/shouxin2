package com.shouxin.shouxin.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.shouxin.shouxin.Adapter.MenuAdapter;
import com.shouxin.shouxin.Adapter.RvDividerItemDecoration;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.SPHelper;
import com.shouxin.shouxin.databinding.FragmentPersonalBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {

    private ArrayList<String> mdata;
    private RecyclerView recyclerView;
    private static volatile PersonalFragment fragment;
    private FragmentPersonalBinding binding;

    public PersonalFragment() {
        // Required empty public constructor
        mdata = new ArrayList<>();
        final String item1 = "历史记录";
        final String item2 = "版本更新";
        final String item3 = "关于";
        mdata.add(item1);
        mdata.add(item2);
        mdata.add(item3);
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
        binding.username.setText(SPHelper.getUsername(getContext()));
        recyclerView = binding.menu;
        recyclerView.setAdapter(new MenuAdapter(this.mdata));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RvDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        return binding.getRoot();
    }


}
