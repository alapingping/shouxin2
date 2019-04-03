package com.shouxin.shouxin.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;

import com.shouxin.shouxin.R;
import com.shouxin.shouxin.fragment.CommunityFragment;
import com.shouxin.shouxin.fragment.ContentFragment;
import com.shouxin.shouxin.fragment.DictionaryFragment;
import com.shouxin.shouxin.fragment.ModeChoiceFragment;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft = fm.beginTransaction();
                    ft.hide(ModeChoiceFragment.getInstance())
                            .hide(DictionaryFragment.getInstance())
                            .show(CommunityFragment.getInstance())
                            .commit();
                    return true;
                case R.id.navigation_study:
                    ft = fm.beginTransaction();
                    ft.hide(ModeChoiceFragment.getInstance())
                            .hide(CommunityFragment.getInstance())
                            .show(DictionaryFragment.getInstance())
                            .commit();
                    return true;
                case R.id.navigation_recognition:
                    ft = fm.beginTransaction();
                    ft.hide(CommunityFragment.getInstance())
                            .hide(DictionaryFragment.getInstance())
                            .show(ModeChoiceFragment.getInstance())
                            .commit();
                    return true;
                case R.id.navigation_personal:
                    ft = fm.beginTransaction();
                    ft.hide(CommunityFragment.getInstance())
                            .hide(DictionaryFragment.getInstance())
                            .show(ModeChoiceFragment.getInstance())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_frame, ModeChoiceFragment.getInstance())
                .add(R.id.container_frame, DictionaryFragment.getInstance())
                .add(R.id.container_frame, CommunityFragment.getInstance())
                .hide(ModeChoiceFragment.getInstance())
                .hide(DictionaryFragment.getInstance())
                .show(CommunityFragment.getInstance())
                .commit();
    }



}
