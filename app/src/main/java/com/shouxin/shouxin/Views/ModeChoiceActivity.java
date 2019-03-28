package com.shouxin.shouxin.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shouxin.shouxin.R;

public class ModeChoiceActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_choice);
    }

    public void onClickPhotoMode(View view) {
        Intent intent = new Intent(this,CapturePhotoActivity.class);
        startActivity(intent);
    }

    public void onClickVideoMode(View view) {
        Intent intent = new Intent(this,TakeTrainSetActivity.class);
        startActivity(intent);
    }
}