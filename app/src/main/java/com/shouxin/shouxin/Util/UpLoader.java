package com.shouxin.shouxin.Util;

import android.graphics.Bitmap;
import android.util.Log;

import com.shouxin.shouxin.API.Client;
import com.shouxin.shouxin.API.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UpLoader {

    public void uploadFileAndString(String actionUrl, String newName, InputStream ffStream) {
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
                        Log.d("--Baidu-output:", jsonObject.getJSONArray("results").getJSONObject(0).get("name").toString());
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

    public void saveBitmap(Bitmap bm, String picName) {
        File f = new File("/sdcard/trainset/", picName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
