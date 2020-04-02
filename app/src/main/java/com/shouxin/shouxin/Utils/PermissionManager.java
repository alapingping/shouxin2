package com.shouxin.shouxin.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

/**
 * Created by 16301 on 2018/9/25 0025.
 */

public class PermissionManager {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static String[] PERMISSION_INTERNET ={
            Manifest.permission.INTERNET
    };

    //获取sd卡读写权限
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

    }
    //获取网络访问权限
    public static void verifyAccessNetPermissions(Activity activity){
        //check if we have access net permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);

        if(permission != PackageManager.PERMISSION_GRANTED){
            //we don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity,PERMISSION_INTERNET,1);
        }

    }




}
