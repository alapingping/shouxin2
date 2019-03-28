package com.shouxin.shouxin.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 *
 * this class is mainly to handle picture
 * including:
 * 1.transfer picture from jpeg to base64
 * 2.transfer bitmap from jpeg to base64
 * 3.transfer picture from colorful to gray bitmap
 * 4.save bitmap as jpeg
 */
public class PictureProcessUtil {

    //transfer picture from jpeg to base64
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }


    //transfer bitmap from jpeg to base64
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    //this class is to transfer picture from colorful to gray
    public static Bitmap getGrayBitmap(Bitmap bm) {
        Bitmap bitmap = null;
        //获取图片的宽和高
        int width = bm.getWidth();
        int height = bm.getHeight();
        //创建灰度图片
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //创建画布
        Canvas canvas = new Canvas(bitmap);
        //创建画笔
        Paint paint = new Paint();
        //创建颜色矩阵
        ColorMatrix matrix = new ColorMatrix();
        // 设置颜色矩阵的饱和度:0代表灰色,1表示原图
        matrix.setSaturation(0);
        // 颜色过滤器
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(matrix);
        // 设置画笔颜色过滤器
        paint.setColorFilter(cmcf);
        // 画图
        canvas.drawBitmap(bm, 0,0, paint);
        return bitmap;
    }

    //save bitmap as jpeg
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
