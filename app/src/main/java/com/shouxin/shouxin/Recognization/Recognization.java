package com.shouxin.shouxin.Recognization;

import android.graphics.Bitmap;

import java.io.IOException;

public interface Recognization {

    //
    int INPUT_SIZE = 224;
    int IMAGE_MEAN = 128;
    float IMAGE_STD = 1;
    String INPUT_NAME = "input";
    String OUTPUT_NAME = "final_result";
//    String LABEL_FILE = "file:///android_asset/model/imagenet_comp_graph_label_strings.txt";
//    String MODEL_FILE = "file:///android_asset/model/tensorflow_inception_graph.pb";
    String LABEL_FILE = "file:///android_asset/model/retrained_labels_rec.txt";
    String MODEL_FILE = "file:///android_asset/model/retrained_graph_rec.pb";

//    String INPUT_NAME = "Mul";
//    String OUTPUT_NAME = "final_result";
//    String LABEL_FILE = "file:///android_asset/model/output_labels.txt";
//    String MODEL_FILE = "file:///android_asset/model/output_graph.pb";

    //压缩bitmap方法
    Bitmap getScaleBitmap(Bitmap bitmap, int size) throws IOException;
    //图片分类方法
    void startImageClassifier(final Bitmap bitmap);

}
