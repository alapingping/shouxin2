package com.shouxin.shouxin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.shouxin.shouxin.R;
import com.shouxin.shouxin.databinding.ActivityMessageDetailBinding;

public class MessageDetailActivity extends AppCompatActivity {

    private ActivityMessageDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}
