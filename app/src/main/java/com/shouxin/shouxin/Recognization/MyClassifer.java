package com.shouxin.shouxin.Recognization;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.IOException;

public class MyClassifer implements Recognization{


    /**
     * 对图片进行缩放
     * @param bitmap
     * @param size
     * @return
     * @throws IOException
     */
    @Override
    public Bitmap getScaleBitmap(Bitmap bitmap, int size) throws IOException {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) size) / width;
        float scaleHeight = ((float) size) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 开始图片识别匹配
     * @param bitmap
     */
    @Override
    public void startImageClassifier(Bitmap bitmap) {

    }
}
