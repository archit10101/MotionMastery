package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import android.graphics.Bitmap;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class MyDemoSingleton {
    private static MyDemoSingleton instance;

    private DemoInfo demoInfo;
    private Bitmap[] bitmaps;

    private Bitmap[] userBitmaps;

    private Bitmap[] reflectedBitmaps;

    private String[] userTimes;


    private MyDemoSingleton(DemoInfo demo, Bitmap[] bitmaps, Bitmap[] reflected) {
        this.demoInfo = demo;
        this.bitmaps = bitmaps;
        userBitmaps = new Bitmap[12];
        userTimes = new String[12];
        reflectedBitmaps =  reflected;
    }

    public static synchronized MyDemoSingleton getInstance(DemoInfo demo, Bitmap[] bitmaps, Bitmap[] reflected) {
        instance = new MyDemoSingleton(demo, bitmaps, reflected);
        return instance;
    }
    public static synchronized MyDemoSingleton getInstance() {
        return instance;
    }

    public Bitmap[] getReflectedBitmaps() {
        return reflectedBitmaps;
    }

    public String[] getUserTimes() {
        return userTimes;
    }

    public DemoInfo getDemoInfo() {
        return demoInfo;
    }
    public Bitmap[] getUserBitmaps() {
        return userBitmaps;
    }
    public Bitmap[] getBitmaps() {
        return bitmaps;
    }
}
