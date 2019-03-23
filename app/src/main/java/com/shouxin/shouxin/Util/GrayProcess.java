package com.shouxin.shouxin.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by 16301 on 2018/10/16 0016.
 */

public class GrayProcess {

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

    }
