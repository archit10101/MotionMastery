package com.example.techniqueshoppebackendconnectionattempt1.Practice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;


import com.example.techniqueshoppebackendconnectionattempt1.AddVideoCreator;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.MyDemoSingleton;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;


public class DisplayVideoForStepFinderActivity extends AppCompatActivity {



    private PlayerView playerView;
    private ExoPlayer player;

    private TextView opacityLabel, instructions;
    private SeekBar opacitySeekBar;
    private FrameLayout frameLayout;
    private ImageView imageView;
    private Button deleteButton, selectButton;
    private ImageButton leftArrowButton, rightArrowButton;

    private Button finishButton;

    private int step;

    private MyDemoSingleton demoSingleton;

    private String videoUri;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_display_video_for_step_finder);





            playerView = findViewById(R.id.exoPlayerView);
            opacityLabel = findViewById(R.id.opacityLabel);
            opacitySeekBar = findViewById(R.id.opacitySeekBar);
            frameLayout = findViewById(R.id.frameLayout);
            imageView = findViewById(R.id.imageView);
            instructions = findViewById(R.id.instructions);
            deleteButton = findViewById(R.id.deleteButton);
            selectButton = findViewById(R.id.selectButton);
            leftArrowButton = findViewById(R.id.leftButton);
            rightArrowButton = findViewById(R.id.rightButton);

            finishButton = findViewById(R.id.checkButton);

            demoSingleton = MyDemoSingleton.getInstance();
            videoUri = getIntent().getStringExtra("vidURI");

            imageView.setAlpha(0f);
            step = 1;

            instructions.setText("Select Step "+step);
            imageView.setImageBitmap(demoSingleton.getBitmaps()[step-1]);

            leftArrowButton.setOnClickListener(v -> {
                if (step>1){
                    step--;
                    instructions.setText("Select Step "+step);
                    imageView.setImageBitmap(demoSingleton.getBitmaps()[step-1]);
                    if (demoSingleton.getUserTimes()[step-1] != null){
                        player.seekTo(Long.parseLong(demoSingleton.getUserTimes()[step-1])/1000);
                    }

                }
            });

            rightArrowButton.setOnClickListener(v -> {
                if (step<12){
                    if (demoSingleton.getBitmaps()[step] != null){
                        step++;
                        instructions.setText("Select Step "+step);
                        imageView.setImageBitmap(demoSingleton.getBitmaps()[step-1]);
                        if (demoSingleton.getUserTimes()[step-1] != null){
                            player.seekTo(Long.parseLong(demoSingleton.getUserTimes()[step-1])/1000);
                        }
                    }
                }
            });

            selectButton.setOnClickListener(v -> {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(DisplayVideoForStepFinderActivity.this, Uri.parse(videoUri));
                String frameTime = String.valueOf(player.getCurrentPosition() * 1000); // Convert to microseconds
                Bitmap frameBitmap = retriever.getFrameAtTime(Long.parseLong(frameTime));
                demoSingleton.getUserBitmaps()[step-1] = frameBitmap;
                demoSingleton.getUserTimes()[step-1] = frameTime;
            });

            deleteButton.setOnClickListener(v -> demoSingleton.getUserBitmaps()[step-1] = null);


            finishButton.setOnClickListener(v -> {
                boolean itWillBeOk = true;
                for (int i =0;i<12;i++){
                    if (demoSingleton.getBitmaps()[i] != null){
                        if (demoSingleton.getUserBitmaps()[i] == null){
                            Toast.makeText(this, "We have got a problem.", Toast.LENGTH_SHORT).show();
                            itWillBeOk = false;
                        }
                    }
                }
                if (itWillBeOk){
                    Toast.makeText(this,"Great Job!",Toast.LENGTH_SHORT).show();
                }
            });

            opacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float opacity = progress / 100f;
                    imageView.setAlpha(opacity);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });



            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUri));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            player.release();
        }
    }
