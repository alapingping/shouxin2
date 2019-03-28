package com.shouxin.shouxin.Views;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shouxin.shouxin.R;
import com.shouxin.shouxin.Utils.PictureProcessUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class TakeTrainSetActivity extends AppCompatActivity {

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

    //图片序号显示控件
    TextView pictureOrderText;

    //开始,暂停,停止按钮
    Button stopBtn;
    Button beginBtn;
    Button pauseBtn;

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

    private boolean beginTakePhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_train_set);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        sView = this.findViewById(R.id.surfaceView);
        sHolder = sView.getHolder();
        sHolder.addCallback(new SurfaceHolder.Callback() {
            @Override			public void surfaceDestroyed(SurfaceHolder holder) {				if(camera != null){					camera.setPreviewCallback(null);					if(isPreview)						camera.stopPreview();					camera.release();					camera = null;				}			}
            @Override			public void surfaceCreated(SurfaceHolder holder) {				initCamera();			}
            @Override			public void surfaceChanged(SurfaceHolder holder, int format, int width,					int height) {										}		});

        pictureOrderText = findViewById(R.id.order);

        beginBtn = findViewById(R.id.begin);
        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginTakePhoto = true;
                beginBtn.setClickable(false);
                pauseBtn.setClickable(true);
                beginBtn.setBackgroundColor(getResources().getColor(R.color.colorHalfTransparentBlack));
                pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorAquaDark));
            }
        });

        pauseBtn = findViewById(R.id.pause);
        pauseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                beginTakePhoto = false;
                beginBtn.setClickable(true);
                pauseBtn.setClickable(false);
                beginBtn.setBackgroundColor(getResources().getColor(R.color.colorMint));
                pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorHalfTransparentBlack));
            }
        });

        stopBtn = findViewById(R.id.stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
    }


    //实现PreviewCallback类
    class StreamIt implements Camera.PreviewCallback{
        private String ipname;
        //帧序号
        public int frameOrder = 0;
        public StreamIt(String ipname)
        {
            this.ipname  = ipname;
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (beginTakePhoto == true) {
                frameOrder++;
                if (frameOrder % 30 == 0 || frameOrder == 0) {
                    Camera.Size size = camera.getParameters().getPreviewSize();
                    System.out.println(ipname);
                    try {
                        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        if (image != null) {
                            //新建字节流
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            //压缩图片质量
                            image.compressToJpeg(new Rect(0, 0, size.width, size.height), 60, stream);
                            //将字节流转为位图
                            Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                            Matrix matrix = new Matrix();
                            matrix.postRotate((float) 90.0);
                            Bitmap rotaBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                            Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, 600, 800, true);
                            Bitmap rectBmp = Bitmap.createBitmap(sizeBitmap, 75, 250, 400, 400);

                            //利用时间戳为图片命名防止覆盖
                            String pictureName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            final InputStream isBm = new ByteArrayInputStream(stream.toByteArray());

                            //利用反射调用工具类方法存储图片
                            Class<?> clazz = Class.forName("com.shouxin.shouxin.Utils.PictureProcessUtil");
                            PictureProcessUtil processUtil = (PictureProcessUtil) clazz.newInstance();
                            Method savePictureMethod = clazz.getMethod("saveBitmap", Bitmap.class, String.class);
                            savePictureMethod.invoke(processUtil, rectBmp, pictureName);
//                            PictureNetworkOperator upLoader = new PictureNetworkOperator();
//                            upLoader.saveBitmap(rectBmp, pictureName);
                            pictureOrderText.setText("当前图片序号:" + String.valueOf((frameOrder / 30) + 1));

                            stream.flush();
                        }
                    } catch (Exception e) {
                        Log.d("---------output:", e.getMessage());
                    }
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

}
