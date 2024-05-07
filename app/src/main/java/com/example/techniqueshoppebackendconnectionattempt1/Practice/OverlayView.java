package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.google.mediapipe.tasks.components.containers.Connection;
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

import java.util.List;
import java.util.Objects;

public class OverlayView extends View {

    private PoseLandmarkerResult results;
    private Paint pointPaint;
    private Paint linePaint;
    private Paint textPaint;



    private float scaleFactor = 1f;

    private float transX;
    private float transY;
    private int imageWidth = 1;
    private int imageHeight = 1;

    private Angler anglesForResults;



    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    private void initPaints() {
        linePaint = new Paint();
        linePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        linePaint.setStrokeWidth(8f);
        linePaint.setStyle(Paint.Style.STROKE);


        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(70);

        pointPaint = new Paint();
        pointPaint.setColor(Color.BLACK);
        pointPaint.setStrokeWidth(12f);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    public void clear() {
        results = null;
        pointPaint.reset();
        linePaint.reset();
        invalidate();
        initPaints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (key.equals("all")){
            if (results != null) {
                for (List<NormalizedLandmark> landmark : results.landmarks()) {
                    Log.d("num","how many");
                    anglesForResults = new Angler(landmark);
                    for (NormalizedLandmark normalizedLandmark : landmark) {
                        canvas.drawPoint(
                                transX+(normalizedLandmark.x() * imageWidth * scaleFactor),
                                transY+(normalizedLandmark.y() * imageHeight * scaleFactor),
                                pointPaint
                        );
                        Log.d("Angles", normalizedLandmark.x() * imageWidth * scaleFactor+"");
                    }


                    for (Connection poseLandmark : PoseLandmarker.POSE_LANDMARKS) {
                        canvas.drawLine(
                                transX+landmark.get(poseLandmark.start()).x() * imageWidth * scaleFactor,
                                transY+landmark.get(poseLandmark.start()).y() * imageHeight * scaleFactor,
                                transX+landmark.get(poseLandmark.end()).x() * imageWidth * scaleFactor,
                                transY+landmark.get(poseLandmark.end()).y() * imageHeight * scaleFactor,
                                linePaint);
                    }

                }
            }
        }else{
            if (results != null) {
                for (List<NormalizedLandmark> landmark : results.landmarks()) {
                    Log.d("num","how many");
                    anglesForResults = new Angler(landmark);
                    Log.d("key",key);
                    for(String str:anglesForResults.getAllKeys()){
                        Log.d("str",str);
                    }
                    int ang1 = Objects.requireNonNull(anglesForResults.getAngleDictionary().get(key))[0];
                    int ang2 = Objects.requireNonNull(anglesForResults.getAngleDictionary().get(key))[1];
                    int ang3 = Objects.requireNonNull(anglesForResults.getAngleDictionary().get(key))[2];
                    canvas.drawPoint(
                            transX+(landmark.get(ang1).x() * imageWidth * scaleFactor),
                            transY+(landmark.get(ang1).y() * imageHeight * scaleFactor),
                            pointPaint
                    );
                    canvas.drawLine(
                            transX+landmark.get(ang1).x() * imageWidth * scaleFactor,
                            transY+landmark.get(ang1).y() * imageHeight * scaleFactor,
                            transX+landmark.get(ang2).x() * imageWidth * scaleFactor,
                            transY+landmark.get(ang2).y() * imageHeight * scaleFactor,
                            linePaint);
                    canvas.drawPoint(
                            transX+(landmark.get(ang2).x() * imageWidth * scaleFactor),
                            transY+(landmark.get(ang2).y() * imageHeight * scaleFactor),
                            pointPaint
                    );
                    canvas.drawLine(
                            transX+landmark.get(ang2).x() * imageWidth * scaleFactor,
                            transY+landmark.get(ang2).y() * imageHeight * scaleFactor,
                            transX+landmark.get(ang3).x() * imageWidth * scaleFactor,
                            transY+landmark.get(ang3).y() * imageHeight * scaleFactor,
                            linePaint);
                    canvas.drawPoint(
                            transX+(landmark.get(ang3).x() * imageWidth * scaleFactor),
                            transY+(landmark.get(ang3).y() * imageHeight * scaleFactor),
                            pointPaint
                    );

                }
            }
        }
    }

    private String key = "all";
    public String[] setAngle(int num){
        String[] angleResult = new String[]{"All", "All"};

        if (num==0){
            key = "all";
        }else{
            if (anglesForResults != null){
                String angle = ""+(int)anglesForResults.getAngle(anglesForResults.getAllKeys().get(num-1));
                key = anglesForResults.getAllKeys().get(num-1);
                Log.d("Angle",angle);
                angleResult = new String[]{anglesForResults.getAllKeys().get(num-1), angle};
            }else{
                angleResult = new String[]{"All", "All"};
            }
        }

        invalidate();
        return angleResult;
    }

    public void setResults(PoseLandmarkerResult poseLandmarkerResults, int imageHeight, int imageWidth, float translationX,float translationY,RunningMode runningMode) {
        results = poseLandmarkerResults;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.transX = translationX;
        this.transY = translationY;

        switch (runningMode) {
            case IMAGE:
            case VIDEO:
                scaleFactor = Math.min(getWidth() * 1f / imageWidth, getHeight() * 1f / imageHeight);
                break;
            case LIVE_STREAM:
                scaleFactor = Math.max(getWidth() * 1f / imageWidth, getHeight() * 1f / imageHeight);
                break;
        }
        invalidate();
    }
}
