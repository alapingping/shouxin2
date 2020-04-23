package com.shouxin.shouxin.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shouxin.shouxin.Activity.CapturePhotoActivity;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Activity.TakeTrainSetActivity;
import com.shouxin.shouxin.databinding.FragmentModeChoiceBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModeChoiceFragment extends Fragment {

    private FragmentModeChoiceBinding binding;

    private static volatile ModeChoiceFragment modeChoiceFragment;

    public ModeChoiceFragment() {
        // Required empty public constructor
    }

    public static ModeChoiceFragment getInstance(){
        if(modeChoiceFragment == null){
            synchronized (ModeChoiceFragment.class) {
                if (modeChoiceFragment == null) {
                    modeChoiceFragment = new ModeChoiceFragment();
                }
            }
        }
        return modeChoiceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentModeChoiceBinding.inflate(inflater, container, false);
        binding.picturerecog.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CapturePhotoActivity.class);
            startActivity(intent);
        });

        binding.taketrainset.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TakeTrainSetActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }
}
