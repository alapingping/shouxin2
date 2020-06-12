package com.shouxin.shouxin.Activity;

import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.PermissionManager;
import com.shouxin.shouxin.Utils.SPHelper;
import com.shouxin.shouxin.database.Repository.WordRepository;
import com.shouxin.shouxin.dummy.DummyWords;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements PermissionManager {

    private static final String CURRENT_TAKE_PHOTO_URI = "currentTakePhotoUri";

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        requestPermission();
        initWordsData();
    }

    public void onClickStart(View view) {
//        Intent intent = new Intent(this, Login_Activity.class);
        Intent intent = new Intent(this, BottomNavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void requestPermission() {
        int hasStoragePermission = ActivityCompat.checkSelfPermission(this, STORAGE_PERMISSION);

        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{STORAGE_PERMISSION}, PermissionManager.STORAGE_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //permission denied 显示对话框告知用户必须打开权限 (storagePermission )
                // Should we show an explanation?
                // 当app完全没有机会被授权的时候，调用shouldShowRequestPermissionRationale() 返回false
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 系统弹窗提示授权
                    showNeedStoragePermissionDialog();
                } else {
                    // 已经被禁止的状态，比如用户在权限对话框中选择了"不再显示”，需要自己弹窗解释
                    showMissingStoragePermissionDialog();
                }
            }
        }
    }

    /**
     *  显示缺失权限提示，可再次请求动态权限
     */
    private void showNeedStoragePermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限获取提示")
                .setMessage("必须要有存储权限才能正常使用词典功能")
                .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                .setPositiveButton("确定", (dialog, which) -> ActivityCompat.requestPermissions(WelcomeActivity.this,
                        new String[]{STORAGE_PERMISSION}, STORAGE_PERMISSION_REQUEST_CODE))
                .setCancelable(false)
                .show();
    }


    /**
     *  显示权限被拒提示，只能进入设置手动修改
     */
    private void showMissingStoragePermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限获取失败")
                .setMessage("必须要有存储权限才能正常运行")
                .setNegativeButton("取消", (dialog, which) -> WelcomeActivity.this.finish())
                .setPositiveButton("去设置", (dialog, which) -> startAppSettings())
                .setCancelable(false)
                .show();
    }

    /**
     * 启动应用的设置进行授权
     */
    @Override
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivityForResult(intent, OPEN_SETTING_REQUEST_COED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestPermission();
    }

    private void initWordsData() {
        compositeDisposable = new CompositeDisposable();
        // 初始化数据库对象
        WordRepository.init(getApplication());

        if (!SPHelper.getInitStatus(this)) {
            SPHelper.setInitStatus(this, true);
            compositeDisposable.add(WordRepository.getWordRepository().deleteAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            Log.d("database----", "删除成功");
                        }
                    }, throwable -> {Log.d("database----", throwable.getMessage());}));
            compositeDisposable.add(
            WordRepository.getWordRepository().insert(DummyWords.getWords())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            Log.d("database----", "插入成功");
                        }
                    }, throwable -> {Log.d("database----", throwable.getMessage());}));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

}
