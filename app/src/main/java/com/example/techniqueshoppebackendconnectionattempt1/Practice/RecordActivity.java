package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.health.connect.datatypes.Record;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techniqueshoppebackendconnectionattempt1.AddVideoCreator;
import com.example.techniqueshoppebackendconnectionattempt1.MainActivity;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.DemoInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.MyDemoSingleton;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class RecordActivity extends AppCompatActivity {

    private ImageButton capture, flipCamera, backButton, forwardButton;
    private ExecutorService service;

    private Recording recording = null;

    private VideoCapture<Recorder> videoCapture = null;
    private PreviewView previewView;

    private SwitchMaterial switchOverlay;

    private ImageView overlayImageview;

    private DemoInfo currentDemo;

    private String vidID;

    private int step;

    private TextView stepText;


    int cameraFacing  = CameraSelector.LENS_FACING_FRONT;



    private RetrofitDBConnector rdbc;

    MyDemoSingleton myDemoSingleton;

    Bitmap[] bitmaps;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (ActivityCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED){
                startCamera(cameraFacing);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        capture = findViewById(R.id.capture);

        myDemoSingleton = MyDemoSingleton.getInstance();
        if (cameraFacing == CameraSelector.LENS_FACING_FRONT){
            bitmaps = myDemoSingleton.getReflectedBitmaps();
        }else{
            bitmaps = myDemoSingleton.getBitmaps();
        }

        step = 1;


        flipCamera = findViewById(R.id.flipCamera);

        switchOverlay = findViewById(R.id.overlaySwitch);

        backButton = findViewById(R.id.leftbutton);
        forwardButton= findViewById(R.id.rightbutton);

        overlayImageview = findViewById(R.id.imageOverlay);
        overlayImageview.setImageBitmap(bitmaps[0]);

        stepText = findViewById(R.id.stepText);


        Toast.makeText(this,vidID,Toast.LENGTH_SHORT).show();
        switchOverlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                backButton.setVisibility(View.VISIBLE);
                forwardButton.setVisibility(View.VISIBLE);
                overlayImageview.setVisibility(View.VISIBLE);
                stepText.setVisibility(View.VISIBLE);
                stepText.setText("Step "+step);

            } else {
                backButton.setVisibility(View.GONE);
                forwardButton.setVisibility(View.GONE);
                overlayImageview.setVisibility(View.GONE);
                stepText.setVisibility(View.GONE);

            }
        });

        rdbc = new RetrofitDBConnector();



        previewView = findViewById(R.id.previewView);

        capture.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){
                activityResultLauncher.launch(Manifest.permission.CAMERA);
            }else if (ActivityCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.RECORD_AUDIO ) != PackageManager.PERMISSION_GRANTED){
                activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }else {
                captureVideo();
            }
        });

        flipCamera.setOnClickListener(v -> {
            if (cameraFacing == CameraSelector.LENS_FACING_FRONT){
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }else{
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            }
            if (cameraFacing == CameraSelector.LENS_FACING_FRONT){
                bitmaps = myDemoSingleton.getReflectedBitmaps();
            }else{
                bitmaps = myDemoSingleton.getBitmaps();
            }
            overlayImageview.setImageBitmap(bitmaps[step-1]);
            startCamera(cameraFacing);
        });

        if (ActivityCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else{
            startCamera(cameraFacing);

        }

        backButton.setOnClickListener(v->{
            if (step>1){
                step--;
                stepText.setText("Step "+step);
                overlayImageview.setImageBitmap(bitmaps[step-1]);
            }
        });

        forwardButton.setOnClickListener(v->{
            if (step<12){
                step++;
                stepText.setText("Step "+step);
                overlayImageview.setImageBitmap(bitmaps[step-1]);
            }
        });

        service = Executors.newSingleThreadExecutor();
    }

    private File outputFile;
    private void captureVideo(){
        capture.setImageResource(R.drawable.baseline_stop_circle_24);
        Recording recording1 = recording;
        if (recording1 != null){
            recording1.stop();
            recording = null;
            return;
        }


        outputFile = null;
        try {
            outputFile = File.createTempFile("video_", ".mp4", getExternalCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (outputFile != null) {
            // Create FileOutputOptions to specify the temporary file
            FileOutputOptions fileOutputOptions = new FileOutputOptions.Builder(outputFile).build();

            recording = videoCapture.getOutput().prepareRecording(RecordActivity.this, fileOutputOptions).start(ContextCompat.getMainExecutor(RecordActivity.this),
                    new Consumer<VideoRecordEvent>() {
                        @Override
                        public void accept(VideoRecordEvent videoRecordEvent) {
                            if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                                capture.setImageResource(R.drawable.baseline_stop_circle_24);
                            } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                                if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                                    String msg = "Video Capture Successful";
                                    Toast.makeText(RecordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Uri uri = Uri.fromFile(outputFile);

                                    Intent intent = new Intent(RecordActivity.this,DisplayVideoForStepFinderActivity.class);
                                    intent.putExtra("vidURI",uri.toString());
                                    startActivity(intent);
                                } else {
                                    recording.close();
                                    recording = null;
                                    String msg = "Error: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getError();
                                    Toast.makeText(RecordActivity.this, msg, Toast.LENGTH_SHORT).show();

                                }
                                capture.setImageResource(R.drawable.baseline_fiber_manual_record_24);

                            }
                        }
                    });
        }
    }

    public void startCamera(int cameraFacing){
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture = ProcessCameraProvider.getInstance(RecordActivity.this);
        processCameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    ProcessCameraProvider provider = processCameraProviderListenableFuture.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    Recorder recorder = new Recorder.Builder()
                            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                            .build();

                    videoCapture = VideoCapture.withOutput(recorder);

                    provider.unbindAll();;

                    CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraFacing).build();

                    Camera camera = provider.bindToLifecycle(RecordActivity.this,cameraSelector, preview,videoCapture);


                }catch (ExecutionException | InterruptedException e){
                    e.printStackTrace();
                }
            }
        },ContextCompat.getMainExecutor(RecordActivity.this));

    }

    private File createTemporaryFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix, getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}