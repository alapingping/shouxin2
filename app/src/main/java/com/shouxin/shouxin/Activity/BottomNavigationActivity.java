package com.shouxin.shouxin.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.shouxin.shouxin.DataModel.Word;
import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.Util;
import com.shouxin.shouxin.database.Repository.WordRepository;
import com.shouxin.shouxin.database.Room.WordRoomDatabase;
import com.shouxin.shouxin.databinding.ActivityBottomNavigationBinding;
import com.shouxin.shouxin.fragment.CommunityFragment;
import com.shouxin.shouxin.fragment.DictionaryFragment;
import com.shouxin.shouxin.fragment.ModeChoiceFragment;
import com.shouxin.shouxin.fragment.PersonalFragment;
import com.shouxin.shouxin.fragment.SearchFragment;
import com.shouxin.shouxin.fragment.WordFragment;

import java.util.ArrayList;

public class BottomNavigationActivity extends AppCompatActivity implements
        WordFragment.OnListFragmentInteractionListener {

    // 所有的fragment
    private ArrayList<Fragment> fragments;
    // viewbinding
    private ActivityBottomNavigationBinding navigationBinding;
    // 监听器
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.container_frame);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment instanceof WordFragment) {
            ft.remove(fragment);
        }
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showFragment(ft, 0);
                navigationBinding.navigationToobar.setTitle("主页");
                break;
            case R.id.navigation_study:
                showFragment(ft, 1);
                navigationBinding.navigationToobar.setTitle("词典");
                break;
            case R.id.navigation_recognition:
                showFragment(ft, 2);
                navigationBinding.navigationToobar.setTitle("识别");
                break;
            case R.id.navigation_personal:
                showFragment(ft, 3);
                navigationBinding.navigationToobar.setTitle("个人中心");
                break;
            default:
                break;
        }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBinding = ActivityBottomNavigationBinding.inflate(LayoutInflater.from(this));
        setContentView(navigationBinding.getRoot());
        initFragments();
        // 初始化数据库对象
        WordRepository.init(getApplication());
        navigationBinding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigationBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

    @Override
    public void onListFragmentInteraction(Word item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("word", item);

        Intent intent = new Intent(this, SingleWordActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.container_frame);
        if(fragment instanceof WordFragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            String lastFragment = fm.getBackStackEntryAt(0).getName();
            fm.popBackStack();
            ft.remove(fragment);
            if (lastFragment.equals("favorite")) {
                showFragment(ft, 3);
            } else if (lastFragment.equals("dictionary")) {
                showFragment(ft, 1);
            }
        } else {
            finish();
        }
        return true;
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(CommunityFragment.getInstance());
        fragments.add(DictionaryFragment.getInstance());
        fragments.add(ModeChoiceFragment.getInstance());
        fragments.add(PersonalFragment.getInstance());
    }

    private void showFragment(FragmentTransaction ft, int index) {
        for (int i = 0; i < fragments.size(); i++) {
            if (i == index) {
                ft.show(fragments.get(i));
            } else {
                ft.hide(fragments.get(i));
            }
        }
        ft.commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        WordRoomDatabase.getDatabase(getApplicationContext()).close();
    }
}
