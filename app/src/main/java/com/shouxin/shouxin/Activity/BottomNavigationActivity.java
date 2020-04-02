package com.shouxin.shouxin.Activity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;

import com.shouxin.shouxin.R;
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
                Fragment curFragment = fm.findFragmentById(R.id.container_frame);
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        ft.hide(curFragment)
//                                .show(CommunityFragment.getInstance())
//                                .commit();
                        ft.replace(R.id.container_frame, CommunityFragment.getInstance());
                        ft.addToBackStack(null);
                        ft.commit();
    //                    return true;
                        break;
                    case R.id.navigation_study:
//                        ft.hide(curFragment)
//                                .show(DictionaryFragment.getInstance())
//                                .commit();
                        ft.replace(R.id.container_frame, DictionaryFragment.getInstance());
                        ft.addToBackStack(null);
                        ft.commit();
    //                    return true;
                        break;
                    case R.id.navigation_recognition:
                        ft.hide(curFragment)
                                .show(ModeChoiceFragment.getInstance())
                                .commit();
    //                    return true;
                        break;
                    case R.id.navigation_personal:
                        ft.hide(curFragment)
                                .show(PersonalFragment.getInstance())
                                .commit();
    //                    return true;
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

        navigationBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
//                .add(R.id.container_frame, ModeChoiceFragment.getInstance())
                .add(R.id.container_frame, DictionaryFragment.getInstance())
//                .add(R.id.container_frame, PersonalFragment.getInstance())
                .add(R.id.container_frame, CommunityFragment.getInstance())
//                .hide(ModeChoiceFragment.getInstance())
                .hide(DictionaryFragment.getInstance())
//                .hide(PersonalFragment.getInstance())
                .show(CommunityFragment.getInstance())
                .commit();
        }
}
