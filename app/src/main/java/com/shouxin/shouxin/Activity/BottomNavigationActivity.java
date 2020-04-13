package com.shouxin.shouxin.Activity;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.Util;
import com.shouxin.shouxin.databinding.ActivityBottomNavigationBinding;
import com.shouxin.shouxin.fragment.CommunityFragment;
import com.shouxin.shouxin.fragment.DictionaryFragment;
import com.shouxin.shouxin.fragment.ModeChoiceFragment;
import com.shouxin.shouxin.fragment.PersonalFragment;
import com.shouxin.shouxin.fragment.SearchFragment;

public class BottomNavigationActivity extends AppCompatActivity {

    private ActivityBottomNavigationBinding navigationBinding;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        ft.hide(DictionaryFragment.getInstance())
                                .hide(ModeChoiceFragment.getInstance())
                                .hide(PersonalFragment.getInstance())
                                .show(CommunityFragment.getInstance());
                        break;
                    case R.id.navigation_study:
                        ft.hide(CommunityFragment.getInstance())
                                .hide(ModeChoiceFragment.getInstance())
                                .hide(PersonalFragment.getInstance())
                                .show(DictionaryFragment.getInstance());
                        break;
                    case R.id.navigation_recognition:
                        ft.hide(CommunityFragment.getInstance())
                                .hide(DictionaryFragment.getInstance())
                                .hide(PersonalFragment.getInstance())
                                .show(ModeChoiceFragment.getInstance());
                        break;
                    case R.id.navigation_personal:
                        ft.hide(CommunityFragment.getInstance())
                                .hide(DictionaryFragment.getInstance())
                                .hide(ModeChoiceFragment.getInstance())
                                .show(PersonalFragment.getInstance());
                        break;
                    default:
                        break;
                }
                ft.commit();
                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBinding = ActivityBottomNavigationBinding.inflate(LayoutInflater.from(this));
        setContentView(navigationBinding.getRoot());
        navigationBinding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigationBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 设置状态栏透明度
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // 判断网络状态
        if (Util.NetworkUtil.isNetworkConnected(this)) {
            if (Util.NetworkUtil.getConnectType(this) ==
                    ConnectivityManager.TYPE_ETHERNET) {
                Util.showMessage(this, "当前正在使用移动网络");
            }
        } else {
            Util.showMessage(getApplicationContext(), "当前网络不可用，请检查网络状态");
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_frame, ModeChoiceFragment.getInstance())
                .add(R.id.container_frame, DictionaryFragment.getInstance())
                .add(R.id.container_frame, PersonalFragment.getInstance())
                .add(R.id.container_frame, CommunityFragment.getInstance())
                .hide(ModeChoiceFragment.getInstance())
                .hide(DictionaryFragment.getInstance())
                .hide(PersonalFragment.getInstance())
                .show(CommunityFragment.getInstance())
                .commit();
        }
}
