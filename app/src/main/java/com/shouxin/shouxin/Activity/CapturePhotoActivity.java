package com.shouxin.shouxin.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.PermissionManager;
import com.shouxin.shouxin.databinding.ActivityCapturePhotoBinding;
import com.shouxin.shouxin.ternsorflow.MyClassifer;
import com.shouxin.shouxin.ternsorflow.Recognization;
import com.shouxin.shouxin.ternsorflow.Classifier;
import com.shouxin.shouxin.ternsorflow.TensorFlowImageClassifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class CapturePhotoActivity extends AppCompatActivity implements PermissionManager {
    //预览界面对象
    private SurfaceView sView = null;
    private String ipName = null;
    //预览界面Holder
    private SurfaceHolder sHolder = null;
    //屏幕尺寸
    private int screenWidth;
    private int screenHeight;
    //相机对象
    private Camera camera = null;

    private boolean isPreview = false;

    // viewbinding
    private ActivityCapturePhotoBinding binding;
    //线程池管理
    private Executor executor;
    //分类器对象
    private Classifier classifier;
    //输出标志
    private final String TAG = "----this is result:";
    //输出结果控件
    private TextView result;
    //拼接结果
    private TextView sentence;

    private Button displaybtn;

    public Camera getCamera() {

        return camera;
    }

    //处理对焦所用变量
    //对焦消息类型
    private int autoFocusMessge = 1001;
    //对焦回调对象
    private Camera.AutoFocusCallback autoFocusCallback;
    //处理对焦事件的handler
    private Handler autoFoucusHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == autoFocusMessge){
                camera.autoFocus(autoFocusCallback);
            }
        }
    };

    //存放结果集
    private ArrayList<String> wordSet;
    //词语出现次数
    private int appearTimes = 0;

    public static final String[] MEANING = new String[]{
            "拥有","山","不","一起","爱",
            "好的", "你","我","曾经","大海",
            "和","吗", "可以","帮助","跨过",
            "吃饭",""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCapturePhotoBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        sView = this.findViewById(R.id.surfaceView);
        sHolder = sView.getHolder();
        sHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if(camera != null) {
                    camera.setPreviewCallback(null);
                    if(isPreview)		 {
                        camera.stopPreview();
                    }
                    camera.release();
                    camera = null;
                }
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {

            }
        });

        wordSet = new ArrayList<>();
        displaybtn = binding.displayrst;
        displaybtn.setOnClickListener(view -> {
            sentence.setText("当前结果:" + getSentence(wordSet));
            wordSet = new ArrayList<>();
        });

        sentence = binding.sentence;
        result = binding.result;

        requestPermission();
    }

    private void initCamera(){
        if(!isPreview) {
            camera = Camera.open();
        }
        if(camera != null && !isPreview){
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(screenWidth, screenHeight);  //设置预览图像的尺寸大小

                parameters.setPreviewFpsRange(10, 20);
                //parameters.setPreviewFrameRate(1);
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    parameters.set("orientation", "portrait");
                    // 镜头角度转90度（默认摄像头是横拍
                    parameters.set("rotation", 90);
                    camera.setDisplayOrientation(90);
                } else{
                    parameters.set("orientation", "landscape");
                    camera.setDisplayOrientation(0);
                }
                //开启相机预览界面
                camera.setPreviewDisplay(sHolder);
                //设置相机回调接口
                camera.setPreviewCallback(new StreamIt("xxx"));
                //正式开始预览
                camera.startPreview();
                //设置相机自动对焦
                autoFocusCallback = new AutoFocusCallBack();
                ((AutoFocusCallBack) autoFocusCallback).setHandler(autoFoucusHandler, autoFocusMessge);

            } catch (IOException e) {

                e.printStackTrace();
            }
            isPreview = true;
        }

        Looper.myQueue().addIdleHandler(idleHandler);
    }

    /**
     *  主线程消息队列空闲时（视图第一帧绘制完成时）处理耗时事件
     */
    MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {

            if (classifier == null) {
                // 创建 Classifier
                classifier = TensorFlowImageClassifier.create(CapturePhotoActivity.this.getAssets(),
                        Recognization.MODEL_FILE,
                        Recognization.LABEL_FILE,
                        Recognization.INPUT_SIZE,
                        Recognization.IMAGE_MEAN,
                        Recognization.IMAGE_STD,
                        Recognization.INPUT_NAME,
                        Recognization.OUTPUT_NAME);
            }

            // 初始化线程池
            executor = new ScheduledThreadPoolExecutor(4, new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("ThreadPool-ImageClassifier");
                    return thread;
                }
            });
            return false;
        }
    };


    // 预览界面回调类
    class StreamIt implements  Camera.PreviewCallback{
        private String ipname;
        //帧序号
        public int frameOrder = 0;
        public StreamIt(String ipname)
        {
            this.ipname  = ipname;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            frameOrder++;
            if(frameOrder%15==0 || frameOrder == 0){
                Camera.Size size = camera.getParameters().getPreviewSize();
                try {
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                    if (image != null) {
                        //新建字节流
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //压缩图片质量
                        image.compressToJpeg(new Rect(0,0,size.width,size.height),60,stream);
                        //将字节流转为位图
                        Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(),0,stream.size());

                        Matrix matrix = new Matrix();
                        matrix.postRotate((float)90.0);
                        Bitmap rotaBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                        Bitmap rectBmp = Bitmap.createBitmap(rotaBitmap,180,620,666,666);

                        //将截取的位图进行压缩并喂入分类模型
                        startImageClassifier(rectBmp);

                        stream.flush();
//                        final InputStream isBm = new ByteArrayInputStream(stream.toByteArray());


//                        FileInputStream fis = new FileInputStream("/sdcard/trainset/after.png");
//                        Bitmap testBmp = BitmapFactory.decodeStream(fis);
//                        新开线程向服务器上传图片
//                        new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new PictureNetworkOperator().uploadFileAndString("http://39.108.60.40:9050/file",
//                                    pictureName, isBm);
//                        }
//                        }).start();

                        //调用远程api进行识别
//                      new PictureNetworkOperator().callRemoteApi(rectBmp);

                        //saveBitmap(bmp ,picture_name);


                    }
                } catch (Exception e) {
                    Log.d("---------output:", e.getMessage());
                }
            }
        }
    }

    //内部自动对焦回调类
    final class AutoFocusCallBack implements Camera.AutoFocusCallback{

        private Handler handler;
        private int autoFocusMessage;

        void setHandler(Handler handler, int autoFocusMessage){
            this.handler = handler;
            this.autoFocusMessage = autoFocusMessage;
        }
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            Log.d("-----callback info:","success");
            handler.sendEmptyMessageDelayed(autoFocusMessage, 500L);
        }
    }

    /**
     * 开始图片识别匹配
     * @param bitmap
     */
    private void startImageClassifier(final Bitmap bitmap) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, Thread.currentThread().getName() + " startImageClassifier");
                    Bitmap croppedBitmap = new MyClassifer().getScaleBitmap(bitmap, Recognization.INPUT_SIZE);
                    final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
                    Log.i(TAG, "startImageClassifier results: " + results);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.setText(String.format("识别结果: %s", getBestWord(results)));
                            WordJoint(MEANING[Integer.valueOf(results.get(0).getTitle()) - 1]);
                            sentence.setText("当前结果:" + getSentence(wordSet));
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "startImageClassifier getScaleBitmap " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void WordJoint(String word){

        //如果当前存入词语过长,则自动将其输出
        if(wordSet.size() > 20){
            sentence.setText("当前结果:" + getSentence(wordSet));
            wordSet = new ArrayList<>();
        }
        //如果结果集为空,则将词语加入
        if(wordSet.size() == 0){
            wordSet.add(word);
            return;
        }
        //如果结果集不为空,且该词语与上一个词语相同,则舍弃该词
        if(word.equals(wordSet.get(wordSet.size() - 1))){
            appearTimes++;
            return;
        }

        //如果该词语与上一个词语不同,且上一个词语的次数为1
        //则取代上一个词语,并清除标志位
        if(!word.equals(wordSet.get(wordSet.size() - 1)) && appearTimes == 1){
            //如果该词语与上上个词语相同,
            //则认为上一个词语是误判,将其删除
            if(wordSet.size() > 1 && word.equals(wordSet.get(wordSet.size() - 2))){
                wordSet.remove(wordSet.size() - 1);
                appearTimes++;
                return;
            }
            wordSet.remove(wordSet.size() - 1);
            wordSet.add(word);
            appearTimes = 1;
            return;
        }

        //如果该词语与上一个词语不同,且上一个词语的次数不为1
        //则将该词语加入结果集
        if(!word.equals(wordSet.get(wordSet.size() - 1)) && appearTimes > 1){
            wordSet.add(word);
            appearTimes = 1;
            return;
        }

    }

    public String getSentence(ArrayList<String> words){
        StringBuffer sb = new StringBuffer();
        for(String word:words){
            sb.append(word);
        }
        return sb.toString();
    }

    public String getBestWord(List<Classifier.Recognition> results){
        String result = null;
        if(results.size() == 0){
            result = "";
        }
        else{
            result = MEANING[Integer.valueOf(results.get(0).getTitle()) - 1]
                    + " 置信度:" + getConfidenceTwoDigits(results.get(0).getConfidence());
        }
        return result;
    }

    private String getConfidenceTwoDigits(float num){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(num).substring(1);
    }

    @Override
    public void requestPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, CAMERA_PERMISSION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE
        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // 用户先择了禁止授予权限
                showNeedCameraPermissionDialog();
            } else {
                // 用户选择了被禁止后不再提示
                showMissingStoragePermissionDialog();
            }
        }
    }

    private void showMissingStoragePermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("相机权限获取失败")
                .setMessage("需要开启相机权限才能使用识别功能")
                .setPositiveButton("去设置", (dialog, which) -> startAppSettings())
                .setNegativeButton("取消", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void showNeedCameraPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("摄像头权限被关闭，请开启权限后重试")
                .setPositiveButton("确定", (dialog, which) -> requestPermission())
                .setNegativeButton("取消", (dialog, which) ->  finish())
                .create()
                .show();
    }


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

}
