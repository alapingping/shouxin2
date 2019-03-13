package com.shouxin.shouxin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 16301 on 2018/9/16 0016.
 */

public class SendFile {

    private Uri imageUri;
    private String ip;
    private int port;
    File file;
    private String newName = "c.jpg";
    private String serverUrl = "http://39.108.60.40:9050/file";
    private HttpURLConnection connection = null;
    private DataOutputStream dos = null;
    private FileInputStream fin = null;
    private static final String BOUNDARY = "-----------------------------1954231646874";
    private byte[] testBuffer;


    String end = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    public SendFile(Uri imageUri){
        this.imageUri = imageUri;
        file = new File(imageUri.getPath());
    }

    public SendFile(byte[] bytes, File file){
        this.testBuffer = new byte[bytes.length];
        System.arraycopy(bytes,0,testBuffer,0,bytes.length);
        this.file = file;
    }

    public SendFile(byte[] bytes, String filename){
        this.testBuffer = new byte[bytes.length];
        System.arraycopy(bytes,0,testBuffer,0,bytes.length);
        newName = filename;
    }


    public String uploadPictureToServer(){

        try{
            URL url = new URL(serverUrl);
            connection = (HttpURLConnection)url.openConnection();

            //允许向url流中读写数据
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //设置POST方法
            connection.setRequestMethod("POST");

            //设置请求头
            connection.setRequestProperty("connection","Keep-Alive");
            //connection.setRequestProperty("Content-Type", BOUNDARY);
            //打开流

            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
        /* 设置DataOutputStream */


            dos = new DataOutputStream(connection.getOutputStream());
            //fin = new FileInputStream(filePath);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; "
                    + "name=\"userfile\";filename=\"" + newName + "\"" + end);
            dos.writeBytes(end);

        /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(file);
        /* 设置每次写入1024bytes */
            int bufferSize = 1024 * 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
        /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
            /* 将资料写入DataOutputStream中 */
                dos.write(buffer, 0, length);
            }
            dos.writeBytes(end);

            // -----
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data;name=\"name\"" + end);
            dos.writeBytes(end + URLEncoder.encode("xiexiezhichi", "UTF-8")
                    + end);
            // -----

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
            fStream.close();
            dos.flush();

            InputStream is = connection.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            dos.close();

            return b.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String uploadPictureToServer2(){

        try{
            URL url = new URL(serverUrl);
            connection = (HttpURLConnection)url.openConnection();

            //允许向url流中读写数据
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            //设置POST方法
            connection.setRequestMethod("POST");

            //设置请求头
            connection.setRequestProperty("connection","Keep-Alive");
            //connection.setRequestProperty("Content-Type", BOUNDARY);
            //打开流

            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
        /* 设置DataOutputStream */


            dos = new DataOutputStream(connection.getOutputStream());
            //fin = new FileInputStream(filePath);

            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; "
                    + "name=\"userfile\";filename=\"" + newName + "\"" + end);
            dos.writeBytes(end);

//        /* 取得文件的FileInputStream */
//            FileInputStream fStream = new FileInputStream(file);
//        /* 设置每次写入1024bytes */
//            int bufferSize = 1024 * 1024;
//            byte[] buffer = new byte[bufferSize];
//
//            int length = -1;
//        /* 从文件读取数据至缓冲区 */
//            while ((length = fStream.read(buffer)) != -1) {
//            /* 将资料写入DataOutputStream中 */
//                dos.write(buffer, 0, length);
//            }
            dos.write(this.testBuffer);
            dos.writeBytes(end);

            // -----
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data;name=\"name\"" + end);
            dos.writeBytes(end + URLEncoder.encode("xiexiezhichi", "UTF-8")
                    + end);
            // -----

            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
         //   fStream.close();
            dos.flush();

            InputStream is = connection.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            dos.close();

            return b.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
