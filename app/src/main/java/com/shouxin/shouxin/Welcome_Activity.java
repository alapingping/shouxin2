package com.shouxin.shouxin;

import com.shouxin.shouxin.PermissionManager.Permission;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Welcome_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);
        Permission permission = new Permission();
        permission.verifyStoragePermissions(Welcome_Activity.this);
        permission.verifyAccessNetPermissions(Welcome_Activity.this);


    }

    public void onClickStart(View view) {
//       Intent intent = new Intent(this, Login_Activity.class);
        Intent intent = new Intent(this, ModeChoiceActivity.class);
        startActivity(intent);
    }

//    public static void compressBitmapToFile(Bitmap bmp, File file) {
//        // 尺寸压缩倍数,值越大，图片尺寸越小
//        int ratio = 2;
//        // 压缩Bitmap到对应尺寸
//        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Config.ARGB_8888);
//        Canvas canvas = new Canvas(result);
//        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
//        canvas.drawBitmap(bmp, null, rect, null);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // 把压缩后的数据存放到baos中
//        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
