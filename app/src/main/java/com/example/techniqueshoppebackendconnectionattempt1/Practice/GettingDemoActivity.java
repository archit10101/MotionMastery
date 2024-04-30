package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.DemoInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.MyDemoSingleton;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;

import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class GettingDemoActivity extends AppCompatActivity {

    private String vidID;

    private DemoInfo currentDemo;
    private FFmpegMediaMetadataRetriever retriever;

    private Bitmap[] allBitmaps;

    private Bitmap[] reflectedBitmaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_demo);

        allBitmaps = new Bitmap[12];
        reflectedBitmaps = new Bitmap[12];


        RetrofitDBConnector rdbc = new RetrofitDBConnector();

        if (getIntent().hasExtra("vidID")){
            vidID = getIntent().getStringExtra("vidID");

        }

        rdbc.getDemosByVideoID(vidID, new RetrofitDBConnector.DemoCallback() {
            @Override
            public void onSuccess(List<DemoInfo> demos) {
                currentDemo = demos.get(0);

                rdbc.downloadFile(currentDemo.getDemoPath(), new RetrofitDBConnector.DownloadCallback() {
                    @Override
                    public void onSuccess(String fileContent) {
                        retriever = new FFmpegMediaMetadataRetriever();
                        retriever.setDataSource(fileContent);
                        for (int i = 0;i<12;i++){
                            if (currentDemo.getImages()[i] != null){
                                Bitmap frameBitmap = retriever.getFrameAtTime(Long.parseLong(currentDemo.getImages()[i]));

                                String rotation = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                                if (rotation != null && frameBitmap != null) {
                                    int rotationAngle = Integer.parseInt(rotation);
                                    if (rotationAngle != 0) {
                                        Matrix matrix = new Matrix();
                                        matrix.postRotate(rotationAngle);
                                        frameBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, frameBitmap.getWidth(), frameBitmap.getHeight(), matrix, true);
                                    }
                                }
                                allBitmaps[i] = frameBitmap;
                                if (frameBitmap != null){
                                    int width = frameBitmap.getWidth();
                                    int height = frameBitmap.getHeight();

                                    Matrix matrix = new Matrix();
                                    matrix.setScale(-1.0f, 1.0f); // Negative x-scale for mirroring

                                    frameBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, width, height, matrix, true);

                                }
                                reflectedBitmaps[i] = frameBitmap;

                            }
                            Log.d("Working","Still working: "+i);
                        }

                        MyDemoSingleton demoSingleton = MyDemoSingleton.getInstance(currentDemo,allBitmaps,reflectedBitmaps);
                        Intent intent = new Intent(GettingDemoActivity.this,RecordActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });

            }

            @Override
            public void onSuccess() {
                Log.d("demo", "wrong onSuccess()?????");
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("demo fail", errorMessage);
            }
        });

    }
}