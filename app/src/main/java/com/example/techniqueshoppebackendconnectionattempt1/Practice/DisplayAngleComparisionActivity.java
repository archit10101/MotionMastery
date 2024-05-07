package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import static android.content.ContentValues.TAG;
import static com.example.techniqueshoppebackendconnectionattempt1.Practice.PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_FULL;
import static com.example.techniqueshoppebackendconnectionattempt1.Practice.PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_HEAVY;
import static com.example.techniqueshoppebackendconnectionattempt1.Practice.PoseLandmarkerHelper.MODEL_POSE_LANDMARKER_LITE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.MyDemoSingleton;
import com.example.techniqueshoppebackendconnectionattempt1.ZoomableFrameLayout;
import com.google.mediapipe.tasks.vision.core.RunningMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DisplayAngleComparisionActivity extends AppCompatActivity {

    private ImageView demoImages,userImages;

    private ImageButton leftButton,rightButton;

    private Button doneButton;

    private TextView scoreText, stepNumText;

    private MyDemoSingleton myDemoSingleton;

    private int step;

    private OverlayView overlayDemo,overlayUser;
    private PoseLandmarkerHelper poseLandmarkerHelperUser;


    private ScheduledExecutorService backgroundExecutor;

    private TextView angleName, userAngleText, demoAngleText;

    private ImageButton leftAngle, rightAngle;

    private int currentAngle= 0;




    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_angle_comparision);
        gestureDetector = new GestureDetector(this, new GestureListener());

        demoImages = findViewById(R.id.imageResultDemo);
        userImages = findViewById(R.id.imageResultUser);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        doneButton = findViewById(R.id.doneButton);
        stepNumText = findViewById(R.id.stepNumText);

        rightAngle = findViewById(R.id.angleRight);


        leftAngle = findViewById(R.id.angleLeft);

        angleName = findViewById(R.id.angleName);
        userAngleText = findViewById(R.id.userAngleDisp);
        demoAngleText = findViewById(R.id.demoAngleDisp);
        overlayDemo = findViewById(R.id.overlayDemo);
        overlayUser = findViewById(R.id.overlayUser);
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
        step = 1;
        myDemoSingleton = MyDemoSingleton.getInstance();


        rightAngle.setOnClickListener(v -> {
            if (currentAngle<10){
                currentAngle++;
                String[] demoAngle = overlayDemo.setAngle(currentAngle);
                String[] userAngle = overlayUser.setAngle(currentAngle);
                angleName.setText(demoAngle[0]);
                demoAngleText.setText(demoAngle[1]+"°");
                userAngleText.setText(userAngle[1]+"°");
                if (!demoAngle[0].equals("All")){
                    if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=15){
                        demoAngleText.setTextColor(Color.RED);
                        userAngleText.setTextColor(Color.RED);
                    }else if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=30){
                        demoAngleText.setTextColor(Color.BLACK);
                        userAngleText.setTextColor(Color.BLACK);
                    } else {
                        demoAngleText.setTextColor(Color.RED);
                        userAngleText.setTextColor(Color.RED);
                    }
                }else {
                    demoAngleText.setTextColor(Color.BLACK);
                    userAngleText.setTextColor(Color.BLACK);
                }

            }
        });
        leftAngle.setOnClickListener(v -> {
            if (currentAngle>0){
                currentAngle--;
                String[] demoAngle = overlayDemo.setAngle(currentAngle);
                String[] userAngle = overlayUser.setAngle(currentAngle);
                angleName.setText(demoAngle[0]);
                demoAngleText.setText(demoAngle[1]+"°");
                userAngleText.setText(userAngle[1]+"°");
                if (!demoAngle[0].equals("All")){
                    if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=15){
                        demoAngleText.setTextColor(Color.RED);
                        userAngleText.setTextColor(Color.RED);
                    }else if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=30){
                        demoAngleText.setTextColor(Color.BLACK);
                        userAngleText.setTextColor(Color.BLACK);
                    } else {
                        demoAngleText.setTextColor(Color.RED);
                        userAngleText.setTextColor(Color.RED);
                    }
                }else {
                    demoAngleText.setTextColor(Color.BLACK);
                    userAngleText.setTextColor(Color.BLACK);
                }
            }
        });

        stepNumText.setText("Step "+step);
        getPose(myDemoSingleton.getUserBitmaps()[step-1],myDemoSingleton.getBitmaps()[step-1],overlayUser,overlayDemo,backgroundExecutor);
        leftButton.setOnClickListener(v -> {
            if (step>1){
                step--;
                Log.d("step",step+"");
                String str = "Step "+step;
                stepNumText.setText(str);
                getPose(myDemoSingleton.getUserBitmaps()[step-1],myDemoSingleton.getBitmaps()[step-1],overlayUser,overlayDemo,backgroundExecutor);
            }
        });

        rightButton.setOnClickListener(v -> {
            if (step<12){
                if (myDemoSingleton.getBitmaps()[step] != null){
                    step++;
                    Log.d("step",step+"");
                    String str = "Step "+step;
                    stepNumText.setText(str);
                    getPose(myDemoSingleton.getUserBitmaps()[step-1],myDemoSingleton.getBitmaps()[step-1],overlayUser,overlayDemo,backgroundExecutor);
                }
            }
        });

        doneButton.setOnClickListener(v -> {
            finish();
        });



    }

    public void getPose(Bitmap userBitmap, Bitmap demoBitmap, OverlayView userOverlay, OverlayView demoOverlay, ScheduledExecutorService backgroundExecutor){

        backgroundExecutor.execute(() -> {

            userImages.setImageBitmap(userBitmap);
            demoImages.setImageBitmap(demoBitmap);

            poseLandmarkerHelperUser = new PoseLandmarkerHelper(
                    PoseLandmarkerHelper.DEFAULT_POSE_DETECTION_CONFIDENCE,
                    PoseLandmarkerHelper.DEFAULT_POSE_TRACKING_CONFIDENCE,
                    PoseLandmarkerHelper.DEFAULT_POSE_PRESENCE_CONFIDENCE,
                    MODEL_POSE_LANDMARKER_LITE,
                    PoseLandmarkerHelper.DELEGATE_CPU,
                    RunningMode.IMAGE,
                    DisplayAngleComparisionActivity.this, null);




            PoseLandmarkerHelper.ResultBundle userResult = poseLandmarkerHelperUser.detectImage(userBitmap);

            if (userResult != null) {

                this.runOnUiThread(() -> {
                    Matrix matrix = userImages.getImageMatrix();
                    float[] matrixValues = new float[9];
                    matrix.getValues(matrixValues);
                    float translationX = matrixValues[Matrix.MTRANS_X];
                    float translationY = matrixValues[Matrix.MTRANS_Y];

                    userOverlay.setResults(
                            userResult.getResults().get(0),
                            userBitmap.getHeight(),
                            userBitmap.getWidth(),translationX,translationY,
                            RunningMode.IMAGE
                    );
                });
            } else {
                Log.d("TAG", "Error running pose landmarker.");
            }



            PoseLandmarkerHelper.ResultBundle demoResult = poseLandmarkerHelperUser.detectImage(demoBitmap);

            if (demoResult != null) {

                this.runOnUiThread(() -> {
                    Matrix matrix = demoImages.getImageMatrix();
                    float[] matrixValues = new float[9];
                    matrix.getValues(matrixValues);
                    float translationX = matrixValues[Matrix.MTRANS_X];
                    float translationY = matrixValues[Matrix.MTRANS_Y];

                    demoOverlay.setResults(
                            demoResult.getResults().get(0),
                            demoBitmap.getHeight(),
                            demoBitmap.getWidth(),translationX,translationY,
                            RunningMode.IMAGE
                    );
                });
            } else {
                Log.d("TAG", "Error running pose landmarker.");
            }

            String[] demoAngle = overlayDemo.setAngle(currentAngle);
            String[] userAngle = overlayUser.setAngle(currentAngle);
            angleName.setText(demoAngle[0]);
            demoAngleText.setText(demoAngle[1]+"°");
            userAngleText.setText(userAngle[1]+"°");
            if (!demoAngle[0].equals("All")){
                if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=15){
                    demoAngleText.setTextColor(Color.RED);
                    userAngleText.setTextColor(Color.RED);
                }else if (Math.abs(Integer.parseInt(userAngle[1])-Integer.parseInt(demoAngle[1]))<=30){
                    demoAngleText.setTextColor(Color.BLACK);
                    userAngleText.setTextColor(Color.BLACK);
                } else {
                    demoAngleText.setTextColor(Color.RED);
                    userAngleText.setTextColor(Color.RED);
                }
            }else {
                demoAngleText.setTextColor(Color.BLACK);
                userAngleText.setTextColor(Color.BLACK);
            }



            poseLandmarkerHelperUser.clearPoseLandmarker();

        });


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass touch events to the GestureDetector
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // Inner class implementing GestureDetector.OnGestureListener
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Handle double-tap gesture here
            resetZoomAndPosition(); // Call method to reset zoom and position of ZoomableFrameLayout
            return true;
        }
    }

    private void resetZoomAndPosition() {
        // Get reference to ZoomableFrameLayout and call resetPosition() method
        ZoomableFrameLayout zoomableFrameLayoutUser = findViewById(R.id.userZoomLayout);
        if (zoomableFrameLayoutUser != null) {
            zoomableFrameLayoutUser.resetPosition();
        }
        ZoomableFrameLayout zoomableFrameLayoutDemo = findViewById(R.id.demoZoomLayout);
        if (zoomableFrameLayoutDemo != null) {
            zoomableFrameLayoutDemo.resetPosition();
        }
    }

}