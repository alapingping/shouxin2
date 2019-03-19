package com.shouxin.shouxin.Adapter;

import java.util.ArrayList;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 10;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);

        String content = null;
        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            content = String.valueOf(i);
            data.add(content);
        }

        return data;
    }


}
