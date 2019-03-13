package com.shouxin.shouxin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;
import com.shouxin.shouxin.Util.PictureToBase64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CapturePhotoActivity extends AppCompatActivity {
    private SurfaceView sView = null;
    private String ipName = null;
    private SurfaceHolder sHolder = null;
    private int screenWidth;
    private int screenHeight;
    private Camera camera = null;
    private boolean isPreview = false;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_photo);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //DisplayMetrics dm = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(dm);
        //screenWidth = dm.widthPixels;
        //screenHeight = dm.heightPixels;


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
    }
    private void initCamera(){
        if(!isPreview)
            camera = Camera.open();
        if(camera!=null && !isPreview){
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
                camera.setPreviewDisplay(sHolder);
                camera.setPreviewCallback(new StreamIt("xxx"));
                camera.startPreview();

                autoFocusCallback = new AutoFocusCallBack();
                ((AutoFocusCallBack) autoFocusCallback).setHandler(autoFoucusHandler, autoFocusMessge);

            } catch (IOException e) {

                e.printStackTrace();

            }
            isPreview = true;
        }

    }
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




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
            //就像onActivityResult一样这个地方就是判断你是从哪来的。
            case 222:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            } else {
            // Permission Denied
                Toast.makeText(getApplicationContext(), "相机开启失败", Toast.LENGTH_SHORT)
                    .show();
            }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
}
class  StreamIt implements  Camera.PreviewCallback{
    private String ipname;
    public int count = 0;
    public StreamIt(String ipname)
    {
        this.ipname  = ipname;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        count++;
        if(count%100==0){
            Camera.Size size = camera.getParameters().getPreviewSize();
            System.out.println(ipname);
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                int pic_name = count - 1;
                if (image != null) {
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 60, outstream);
                    //新建字节流
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //压缩图片质量
                    image.compressToJpeg(new Rect(0,0,size.width,size.height),60,stream);
                    //将字节流转为位图
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(),0,stream.size());
                    //为图片命名
                    final String picture_name = pic_name +".jpg";
                    System.out.println(picture_name);
                    final InputStream isBm = new ByteArrayInputStream(stream.toByteArray());

                    uploadFileAndString("http://39.108.60.40:9050/file", picture_name, isBm);//http://192.168.1.130:5000/file
                    //新开线程向服务器上传图片
                  new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uploadFileAndString("http://39.108.60.40:9050/file",
                                    picture_name, isBm);
                        }
                    }).start();

                    //调用远程api进行识别
//                    callRemoteApi(bmp);

                    //saveBitmap(bmp ,picture_name);
                    System.out.println(bmp);
                    pic_name = pic_name + 1;
                    stream.flush();
                }
            } catch (Exception e) {
                Log.d("---------output:", e.getMessage());
            }
        }
    }

    private void uploadFileAndString(String actionUrl, String newName, InputStream ffStream) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        //Handler handler = new Handler();
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"userfile\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);

            /* 取得文件的FileInputStream */
            InputStream fStream = ffStream;
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);

            // -----
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data;name=\"name\"" + end);
            ds.writeBytes(end + URLEncoder.encode("xiexiezhichi", "UTF-8")
                    + end);
            // -----

            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            fStream.close();
            ds.flush();

            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            //handler.sendEmptyMessage(0x12);
            /* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            // handler.sendEmptyMessage(0x13);
        }
    }

    public void callRemoteApi(Bitmap bmp) throws JSONException {
        //使用retrofit调用百度api
        String access_token = "24.ddfb06c45e8ba9fe5043dbd9581b4280.2592000.1553944820.282335-15644269";
        String base64OfPicture = PictureToBase64.bitmapToBase64(bmp);
        Service service = Client.retrofit.create(Service.class);
        //构造请求体
        JSONObject info = new JSONObject();
        info.put("image",base64OfPicture);
        info.put("top_num",5);
        //转换为RequestBody
        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json"), info.toString());
        //异步调用
        Call<ResponseBody> call = service.UploadPicture("shouxin",access_token,body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("---------output:", jsonObject.getJSONArray("results").getJSONObject(0).get("name").toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("---------failure:", t.getMessage());
            }
        });
    }


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

}

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