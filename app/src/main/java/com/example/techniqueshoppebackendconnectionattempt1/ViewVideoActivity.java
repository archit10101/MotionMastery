package com.example.techniqueshoppebackendconnectionattempt1;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.google.android.material.button.MaterialButton;

public class ViewVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private boolean isPlaying = false;
    private boolean isFullscreen = false;

    private Handler handler;
    private Runnable runnable;

    private ImageView back;

    private MaterialButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        videoView = findViewById(R.id.videoView);

        // Initialize Handler
        handler = new Handler();

        back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next = findViewById(R.id.practiceButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to practice.
            }
        });

        // Set the path of the video file
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;

        // Set the Uri
        Uri videoUri = Uri.parse(videoPath);

        // Set up the MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set the video Uri to VideoView
        videoView.setVideoURI(videoUri);

        // Initialize SeekBar
    }
    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }
}
