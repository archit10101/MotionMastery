package com.example.techniqueshoppebackendconnectionattempt1;

import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.techniqueshoppebackendconnectionattempt1.Practice.GettingDemoActivity;
import com.example.techniqueshoppebackendconnectionattempt1.Practice.RecordActivity;
import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.Video;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

public class ViewVideoActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    PlayerView playerView;

    private boolean isPlaying = false;
    private boolean isFullscreen = false;

    private Handler handler;
    private Runnable runnable;

    private ImageView back;

    private MaterialButton next;

    String vidID;

    Video thisVid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        vidID = "1";
        if (getIntent().hasExtra("vidID")){
            vidID = getIntent().getStringExtra("vidID");
        }
        RetrofitDBConnector rdbc = new RetrofitDBConnector();

        rdbc.getVidbyVidID(Integer.parseInt(vidID), new RetrofitDBConnector.VideoSingleCallback() {
            @Override
            public void onSuccess(Video vid) {
                thisVid = vid;
                Log.d("Video Path",vidID+" : "+thisVid.getVideoName()+" : "+thisVid.getVideoPath());
                rdbc.downloadFile(thisVid.getVideoPath(), new RetrofitDBConnector.DownloadCallback() {
                    @Override
                    public void onSuccess(String fileContent) {
                        Log.d("url","m"+fileContent);

                        MediaItem mediaItem = MediaItem.fromUri(fileContent);
                        exoPlayer.setMediaItem(mediaItem);
                        exoPlayer.prepare();
                        exoPlayer.play();


                    }

                    @Override
                    public void onFailure(String error) {
                        // Handle download failure
                        Log.e("Download", "Download failed: " + error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ViewVideoActivity.this, "There was a problem: "+error,Toast.LENGTH_SHORT).show();
            }
        });

        exoPlayer = new ExoPlayer.Builder(this).build();

        playerView = findViewById(R.id.playerView);
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
                Intent intent = new Intent(ViewVideoActivity.this, GettingDemoActivity.class);
                intent.putExtra("vidID",vidID);
                startActivity(intent);
            }
        });



        playerView.setPlayer(exoPlayer);



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
        if (exoPlayer != null) {
            exoPlayer.release(); // Release player resources
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false); // Pause playback
        }
    }


}
