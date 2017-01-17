package com.example.zpwu.svhn;

import android.content.Context;
import android.content.res.AssetManager;

public class DigitDetector {

    static {
        System.loadLibrary("tensorflow_mnist");
    }

    public native int init(AssetManager assetManager, String model);

    public native int detectDigit(int[] pixels);

    public boolean setup(Context context){
        AssetManager assetManager = context.getAssets();

        int ret = init(assetManager, "file:///android_asset/expert-graph.pb");
        return ret >=0;

    }
}
