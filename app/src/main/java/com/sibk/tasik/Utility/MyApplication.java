package com.sibk.tasik.Utility;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static Context mAppContext;
    private static boolean is_development_mode=false;

    private static Context appContext;
    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
                Thread.getDefaultUncaughtExceptionHandler()));
        mInstance = this;
        appContext = getApplicationContext();
        Fresco.initialize(
                appContext,
                ImagePipelineConfig.newBuilder(appContext)
                        .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                        .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                        .experiment().setNativeCodeDisabled(true)
                        .build());
        this.setAppContext(getApplicationContext());
//        Fresco.initialize(this);

    }

    //    public static Context getAppContext() {
//        return appContext;
//    }
    public static MyApplication getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        MyApplication.mAppContext = mAppContext;
    }

    public boolean getIsDevelopmentMode()
    {
        return is_development_mode;
    }
}
