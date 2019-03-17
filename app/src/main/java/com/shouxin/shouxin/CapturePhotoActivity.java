package com.shouxin.shouxin;

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
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.Recognization.MyClassifer;
import com.shouxin.shouxin.Recognization.Recognization;
import com.shouxin.shouxin.TFUtils.FileUtil;
import com.shouxin.shouxin.TFUtils.TFActivity;
import com.shouxin.shouxin.Util.PictureToBase64;
import com.shouxin.shouxin.Util.UpLoader;
import com.shouxin.shouxin.ternsorflow.Classifier;
import com.shouxin.shouxin.ternsorflow.TensorFlowImageClassifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CapturePhotoActivity extends AppCompatActivity {
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

    //线程池管理
    private Executor executor;
    //分类器对象
    private Classifier classifier;
    //输出标志
    private final String TAG = "----this is result:";
    //输出结果控件
    TextView result;
    //拼接结果
    TextView sentence;

    Button displaybtn;

    public Camera getCamera() {

        return camera;
    }

    private static final int OPEN_SETTING_REQUEST_COED = 110;
    private static final int TAKE_PHOTO_REQUEST_CODE = 120;
    private static final int PICTURE_REQUEST_CODE = 911;

    private static final int PERMISSIONS_REQUEST = 108;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 119;

    private static final String CURRENT_TAKE_PHOTO_URI = "currentTakePhotoUri";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        sView = this.findViewById(R.id.surfaceView);
        sHolder = sView.getHolder();
        sHolder.addCallback(new Callback() {
            @Override			public void surfaceDestroyed(SurfaceHolder holder) {				if(camera != null){					camera.setPreviewCallback(null);					if(isPreview)						camera.stopPreview();					camera.release();					camera = null;				}			}
            @Override			public void surfaceCreated(SurfaceHolder holder) {				initCamera();			}
            @Override			public void surfaceChanged(SurfaceHolder holder, int format, int width,					int height) {										}		});

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//
//            }
//        });
        wordSet = new ArrayList<>();
        displaybtn = findViewById(R.id.displayrst);
        displaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence.setText(wordSet.toString());
                wordSet = new ArrayList<>();
            }
        });

        sentence = findViewById(R.id.sentence);
        result = findViewById(R.id.result);
    }

    private void initCamera(){
        if(!isPreview)
            camera = Camera.open();
        if(camera != null && !isPreview){
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(screenWidth, screenHeight);  //设置预览图像的尺寸大小

                parameters.setPreviewFpsRange(10, 20);
                //parameters.setPreviewFrameRate(1);
                if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
                {				      parameters.set("orientation", "portrait");
                    parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍
                    camera.setDisplayOrientation(90);
                } else{
                    parameters.set("orientation", "landscape"); 				      camera.setDisplayOrientation(0);
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
            executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("ThreadPool-ImageClassifier");
                    return thread;
                }
            });
            requestMultiplePermissions();
            return false;
        }
    };

    public void getPermission(){
        /**
         * 该方法判定获取权限的结果
         * 若失败，则不能开启摄像头
         * 若成功，则正确开启摄像头
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},222);
                return;
            }else{
            }
        } else {
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//            switch (requestCode) {
//            //就像onActivityResult一样这个地方就是判断你是从哪来的。
//            case 222:
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // Permission Granted
//            } else {
//            // Permission Denied
//                Toast.makeText(getApplicationContext(), "相机开启失败", Toast.LENGTH_SHORT)
//                    .show();
//            }
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//    }

    //内部StreamIt类???
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
            if(frameOrder%30==0 || frameOrder == 0){
                Camera.Size size = camera.getParameters().getPreviewSize();
                System.out.println(ipname);
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
                        Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, 600, 800, true);
                        Bitmap rectBmp = Bitmap.createBitmap(sizeBitmap,75,250,400,400);

                        String pictureName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        final InputStream isBm = new ByteArrayInputStream(stream.toByteArray());

//                        UpLoader upLoader = new UpLoader();
//                        upLoader.saveBitmap(sizeBitmap, "before.png");
//                        upLoader.saveBitmap(rectBmp, pictureName);

//                        FileInputStream fis = new FileInputStream("/sdcard/trainset/after.png");
//                        Bitmap testBmp = BitmapFactory.decodeStream(fis);
                        //新开线程向服务器上传图片
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            new UpLoader().uploadFileAndString("http://39.108.60.40:9050/file",
//                                    pictureName, isBm);
//                        }
//                    }).start();

                        //调用远程api进行识别
//                    new UpLoader().callRemoteApi(rectBmp);

                        //saveBitmap(bmp ,picture_name);

                        //将截取的位图进行压缩并喂入分类模型
                        startImageClassifier(rectBmp);

                        stream.flush();
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
                    new UpLoader().saveBitmap(croppedBitmap, "new.png");

                    final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
                    Log.i(TAG, "startImageClassifier results: " + results);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.setText(String.format("results: %s", results));
                            WordJoint(results.get(0).getTitle());
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "startImageClassifier getScaleBitmap " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestMultiplePermissions() {

        String storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String cameraPermission = Manifest.permission.CAMERA;

        int hasStoragePermission = ActivityCompat.checkSelfPermission(this, storagePermission);
        int hasCameraPermission = ActivityCompat.checkSelfPermission(this, cameraPermission);

        List<String> permissions = new ArrayList<>();
        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(storagePermission);
        }

        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(cameraPermission);
        }

        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(this, params, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0]) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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
        } else if (requestCode == CAMERA_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showNeedCameraPermissionDialog();
            } else {
                openSystemCamera();
            }
        }
    }

    /**
     *  显示缺失权限提示，可再次请求动态权限
     */
    private void showNeedStoragePermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限获取提示")
                .setMessage("必须要有存储权限才能获取到图片")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(CapturePhotoActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST);
                    }
                }).setCancelable(false)
                .show();
    }


    /**
     *  显示权限被拒提示，只能进入设置手动改
     */
    private void showMissingStoragePermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限获取失败")
                .setMessage("必须要有存储权限才能正常运行")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        CapturePhotoActivity.this.finish();
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showNeedCameraPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("摄像头权限被关闭，请开启权限后重试")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private static final String PACKAGE_URL_SCHEME = "package:";

    /**
     * 启动应用的设置进行授权
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivityForResult(intent, OPEN_SETTING_REQUEST_COED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE) {
                // 处理选择的图片
//                handleInputPhoto(data.getData());
            } else if (requestCode == OPEN_SETTING_REQUEST_COED){
                requestMultiplePermissions();
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                // 如果拍照成功，加载图片并识别
//                handleInputPhoto(currentTakePhotoUri);
            }
        }
    }

    /**
     * 打开系统相机
     */
    private void openSystemCamera() {
        //调用系统相机
        Intent takePhotoIntent = new Intent();
        takePhotoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (takePhotoIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "当前系统没有可用的相机应用", Toast.LENGTH_SHORT).show();
            return;
        }

//        String fileName = "TF_" + System.currentTimeMillis() + ".jpg";
//        File photoFile = new File(FileUtil.getPhotoCacheFolder(), fileName);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //通过FileProvider创建一个content类型的Uri
//            currentTakePhotoUri = FileProvider.getUriForFile(this, "gdut.bsx.tensorflowtraining.fileprovider", photoFile);
//            //对目标应用临时授权该 Uri 所代表的文件
//            takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            currentTakePhotoUri = Uri.fromFile(photoFile);
//        }
//
//        //将拍照结果保存至 outputFile 的Uri中，不保留在相册中
//        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentTakePhotoUri);
        startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    public void WordJoint(String word){
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

}






//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


//    public void saveBitmap() {
//        File f = new File("/sdcard/namecard/", picName);
//        if (f.exists()) {
//            f.delete();
//        }
//        try {
//            FileOutputStream out = new FileOutputStream(f);
//            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.flush();
//            out.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }