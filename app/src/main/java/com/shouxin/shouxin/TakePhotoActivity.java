package com.shouxin.shouxin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.shouxin.shouxin.PictureProcess.GrayProcess;

public class TakePhotoActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static String[] PERMISSION_INTERNET = {
            Manifest.permission.INTERNET
    };
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button takePhotoButton;
    private ImageView picture;
    private Uri imageUri;
    private File outputImage;
    private File outputGrayImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        takePhotoButton = (Button) findViewById(R.id.take_photo);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 我们使用Intent的方式打开系统相机
                 * 1.直接跳转相机包名，前提是你知道这个应用的包名
                 * 2.就是使用隐式Intent了，在这里我们就使用隐式intent
                 */
                //确认获取相机权限
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(TakePhotoActivity.this,new String[]{Manifest.permission.CAMERA},222);
                        return;
                    }else{

                        TakePhotoMethod();
                    }
                } else {

                    TakePhotoMethod();
                }
            }
        });
    }

    /**
     * 该方法判定获取权限的结果
     * 若失败，则不能开启摄像头
     * 若成功，则正确开启摄像头
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    TakePhotoMethod();
                } else {
                    // Permission Denied
                    Toast.makeText(TakePhotoActivity.this, "相机开启失败", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //打开相机所调用的方法
    public void TakePhotoMethod(){

        outputImage = new File(Environment. getExternalStorageDirectory(), "tempImage.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //指定拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);//启动摄像头

    }
    //调用startActivityForResult时启用该方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO://拍摄完成
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO://剪裁完成
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver()
                                        .openInputStream(imageUri));

                        bitmap = GrayProcess.getGrayBitmap(bitmap);
                        outputGrayImage = new File(Environment. getExternalStorageDirectory(),
                                "GraytempImage.jpg");
                        try {
                            if (outputGrayImage.exists()) {
                                outputGrayImage.delete();
                            }
                            outputGrayImage.createNewFile();
                            FileOutputStream fos = new FileOutputStream(outputGrayImage);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                        picture.setImageBitmap(bitmap); // 将裁剪后的照片显示出来
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //处理服务器返回消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                TextView textView = (TextView)findViewById(R.id.result_view);
                Toast.makeText(TakePhotoActivity.this,"识别完成",Toast.LENGTH_SHORT).show();
                textView.setText("识别结果:" + msg.obj.toString());
            }
        }
    };

    public void onRecognization(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SendFile sendFile = new SendFile(imageUri);
                String respond = sendFile.uploadPictureToServer();


                Message message = Message.obtain();
                message.what = 1;
                message.arg1 = 2;
                message.arg2 = 3;
                message.obj = respond;

               handler.sendMessage(message);
            }
        }).start();
    }

    private void LoadBitmap(Uri imageUri){

        try {
            Bitmap bitmap = BitmapFactory.decodeStream
                    (getContentResolver()
                            .openInputStream(this.imageUri));

            bitmap = GrayProcess.getGrayBitmap(bitmap);
            outputGrayImage = new File(Environment. getExternalStorageDirectory(), "GraytempImage.jpg");
            try {
                if (outputGrayImage.exists()) {
                    outputGrayImage.delete();
                }
                outputGrayImage.createNewFile();
                FileOutputStream fos = new FileOutputStream(outputGrayImage);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }



            picture.setImageBitmap(bitmap); // 将裁剪后的照片显示出来
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




    }

}




