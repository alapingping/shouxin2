package com.shouxin.shouxin.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shouxin.shouxin.Views.CapturePhotoActivity;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Views.TakeTrainSetActivity;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModeChoiceFragment extends Fragment implements ScreenShotable {

    Button takePicBtn;
    Button takeTrainsetBtn;

    private static ModeChoiceFragment modeChoiceFragment = new ModeChoiceFragment();

    public ModeChoiceFragment() {
        // Required empty public constructor
    }

    public static ModeChoiceFragment getInstance(){
        if(modeChoiceFragment == null){
            modeChoiceFragment = new ModeChoiceFragment();
        }
        return modeChoiceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mode_choice, container, false);
        takePicBtn = view.findViewById(R.id.picturerecog);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CapturePhotoActivity.class);
                startActivity(intent);
            }
        });

        takeTrainsetBtn = view.findViewById(R.id.taketrainset);
        takeTrainsetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TakeTrainSetActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }
}
