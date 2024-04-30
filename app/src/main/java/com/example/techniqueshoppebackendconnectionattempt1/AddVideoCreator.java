package com.example.techniqueshoppebackendconnectionattempt1;

import android.Manifest;
import androidx.lifecycle.LifecycleOwner;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.DemoInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.PresignedUrlResponse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.VideoTutorial;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AddVideoCreator extends AppCompatActivity {

    private ImageView vid_close;
    private TextInputEditText editVidName;
    private TextInputEditText editVidDescription;
    private MaterialButton buttonUploadVideo;

    private MaterialButton buttonUploadDemo;

    private MaterialButton buttonCreateVideo;

    private MaterialButton vidDelete,vidSelect, demoDelete, demoSelect,rightdemoButton,leftDemoButton;

    private LinearLayout vidController,demoController;


    private ExoPlayer exoPlayerVideo, exoPlayerDemo;

    private TextView instructionsVid, instructionsDemo;

    private PlayerView vidPlayerView, demoPlayerView;
    private String videoUri;

    String courseId;
    RetrofitDBConnector rdbc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_creator);


        if (getIntent().hasExtra("COURSE_ID")){
            courseId = getIntent().getStringExtra("COURSE_ID");
        }else{
            Toast.makeText(this,"You need a courseID",Toast.LENGTH_SHORT).show();
        }
        vid_close = findViewById(R.id.vid_close);
        editVidName = findViewById(R.id.editVidName);
        editVidDescription = findViewById(R.id.editVidDescription);
        buttonUploadVideo = findViewById(R.id.button_upload_video);
        buttonCreateVideo = findViewById(R.id.button_create_video);
        buttonUploadDemo = findViewById(R.id.button_upload_demo);
        instructionsDemo = findViewById(R.id.demo_instructions);
        instructionsVid = findViewById(R.id.instructions);
        demoPlayerView = findViewById(R.id.demo_playerView);
        vidPlayerView = findViewById(R.id.playerView);

        vidController = findViewById(R.id.videoControls);
        demoController = findViewById(R.id.demoControls);
        vidDelete = findViewById(R.id.vidDeleteButton);
        vidSelect = findViewById(R.id.vidSelectButton);
        demoDelete = findViewById(R.id.demoDeleteControl);
        demoSelect = findViewById(R.id.demoSelectButton);

        rightdemoButton = findViewById(R.id.rightDemoButton);
        leftDemoButton = findViewById(R.id.leftDemoButton);

        rdbc = new RetrofitDBConnector();

        videoUri = "blank";
        vid_close.setOnClickListener(v -> {
            finish();
        });

        buttonUploadDemo.setOnClickListener(v->{
            Log.d("d","HERE");
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openDemoPicker();
            }

        });
        buttonUploadVideo.setOnClickListener(v -> {
            Log.d("d","HERE");
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImagePicker();
            }

        });

        buttonCreateVideo.setOnClickListener(v -> {
            // Check if video name and description are entered
            String videoName = editVidName.getText().toString().trim();
            String videoDescription = editVidDescription.getText().toString().trim();
            if (TextUtils.isEmpty(videoName)) {
                Toast.makeText(this, "Please enter video name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(videoDescription)) {
                Toast.makeText(this, "Please enter video description: "+videoDescription, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(videoUri)) {
                Toast.makeText(this, "Please enter vidURI", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUrlInputStream == null){
                Toast.makeText(this, "Please enter thumbnail", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(demoUriUuid)){
                Toast.makeText(this, "Please enter demo", Toast.LENGTH_SHORT).show();
                return;
            }


            VideoTutorial videoTutorial = new VideoTutorial(0,Integer.parseInt(courseId),videoName,videoDescription,thumbnailuuid,videoUri,4);
            rdbc.createVideoTutorial(videoTutorial, new RetrofitDBConnector.VideoCallback() {
                @Override
                public void onSuccess(List<VideoTutorial> fileContent) {
                    VideoTutorial myVid = fileContent.get(0);
                    Toast.makeText(AddVideoCreator.this,"Success: "+fileContent.get(0).getVideoName(),Toast.LENGTH_SHORT).show();
                    DemoInfo demo = new DemoInfo("0",""+myVid.getVideoID(),demoUriUuid,frameTimesDemo);
                    Log.d("demo test path",demoUriUuid);
                    rdbc.postNewDemo(demo, new RetrofitDBConnector.DemoCallback() {
                        @Override
                        public void onSuccess(List<DemoInfo> demos) {

                        }

                        @Override
                        public void onSuccess() {
                            Log.d("Demo","Demo is made");
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.d("Demo","demo: "+errorMessage);
                        }
                    });

                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(AddVideoCreator.this,"Failure: "+error,Toast.LENGTH_SHORT).show();

                }
            });



            ContentResolver contentResolver = getApplication().getContentResolver();
            try (InputStream inputStream = contentResolver.openInputStream(vidUri)) {
                if (inputStream == null){
                    Log.d("inputStream","input stream is null");

                }else{
                    rdbc.uploadWithURI(inputStream, urlPresigned, "video/*");
                }
            } catch (IOException e) {
                // Handle potential exceptions during stream opening
                e.printStackTrace();
            }


            try (InputStream inputStream = contentResolver.openInputStream(demoUri)) {
                if (inputStream == null){
                    Log.d("demo Inputstream","input stream is null");

                }else{
                    rdbc.uploadWithURI(inputStream, demoUrlPresigned, "video/*");
                }
            } catch (IOException e) {
                // Handle potential exceptions during stream opening
                e.printStackTrace();
            }

            if (imageUrlInputStream == null){
                Log.d("image","image input stream is null");

            }else{
                try {
                    rdbc.uploadWithURI(imageUrlInputStream, thumbnailurlPresigned, "video/*");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


            //Upload the videothumbnail





        });
    }
    private void openImagePicker() {
        Log.d("I am here","This is openImagePicker");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openDemoPicker() {
        Log.d("I am here","This is openImagePicker");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, 3);
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String uuid;
    private String urlPresigned;

    private Uri vidUri;
    private Uri demoUri;

    private String demoUriUuid;

    private String demoUrlPresigned;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK )
        {
            try {
                Log.d("here","here");
                vidUri = data.getData();



                rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                    @Override
                    public void onUploadSuccess(PresignedUrlResponse uploadObject) {
                        uuid = uploadObject.getUuid();
                        urlPresigned = uploadObject.getUrl();
                        Log.d("uuid",uuid);
                        Log.d("urlPresigned",urlPresigned);

                        startVideoInstructions();

                        videoUri = (uuid);

                    }

                    @Override
                    public void onUploadFailure(String error) {
                        Log.d("imageError",error);
                    }

                });
            } catch (Exception e) {

            }
        }else if (requestCode == 3 && resultCode == RESULT_OK )
        {
            try {
                Log.d("here","here");
                demoUri = data.getData();



                rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                    @Override
                    public void onUploadSuccess(PresignedUrlResponse uploadObject) {
                        uuid = uploadObject.getUuid();
                        demoUrlPresigned = uploadObject.getUrl();
                        Log.d("uuid",uuid);
                        Log.d("urlPresigned",urlPresigned);
                        demoUriUuid = (uuid);

                        startDemoInstructions();


                    }

                    @Override
                    public void onUploadFailure(String error) {
                        Log.d("demoError",error);
                    }

                });
            } catch (Exception e) {

            }
        }
    }

    private String thumbnailuuid;

    private String thumbnailurlPresigned;
    private InputStream imageUrlInputStream;
    public void startVideoInstructions(){
        exoPlayerVideo = new ExoPlayer.Builder(AddVideoCreator.this).build();
        MediaItem mediaItem = MediaItem.fromUri(vidUri);
        exoPlayerVideo.setMediaItem(mediaItem);
        exoPlayerVideo.prepare();
        exoPlayerVideo.play();
        vidPlayerView.setPlayer(exoPlayerVideo);

        vidPlayerView.setVisibility(View.VISIBLE);
        vidController.setVisibility(View.VISIBLE);
        instructionsVid.setVisibility(View.VISIBLE);

        instructionsVid.setText("Select video thumbnail.");

        vidDelete.setOnClickListener(v->{
            imageUrlInputStream = null;
            thumbnailuuid = null;
            thumbnailurlPresigned = null;
        });

        vidSelect.setOnClickListener(v ->{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(AddVideoCreator.this, vidUri);
            String frameTime = String.valueOf(exoPlayerVideo.getCurrentPosition() * 1000); // Convert to microseconds
            Bitmap frameBitmap = retriever.getFrameAtTime(Long.parseLong(frameTime));
            if (frameBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                frameBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageUrlInputStream = new ByteArrayInputStream(stream.toByteArray());
            }
            rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                @Override
                public void onUploadSuccess(PresignedUrlResponse uploadObject) throws IOException {
                    thumbnailuuid = uploadObject.getUuid();
                    thumbnailurlPresigned = uploadObject.getUrl();

                }

                @Override
                public void onUploadFailure(String error) {
                    Log.d("imageError",error);
                }

            });
            // Release the MediaMetadataRetriever
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });






    }

    private int stepNum;

    private String[] frameTimesDemo = new String[12];

    private void startDemoInstructions(){
        exoPlayerDemo = new ExoPlayer.Builder(AddVideoCreator.this).build();
        MediaItem mediaItem = MediaItem.fromUri(demoUri);
        exoPlayerDemo.setMediaItem(mediaItem);
        exoPlayerDemo.prepare();
        exoPlayerDemo.play();
        demoPlayerView.setPlayer(exoPlayerDemo);

        demoPlayerView.setVisibility(View.VISIBLE);
        demoController.setVisibility(View.VISIBLE);
        instructionsDemo.setVisibility(View.VISIBLE);

        instructionsDemo.setText("Select Step 1");
        stepNum = 1;

        leftDemoButton.setOnClickListener(v->{
            if (stepNum>1){
                stepNum--;
                instructionsDemo.setText("Select Step "+stepNum);
                if (frameTimesDemo[stepNum-1] != null){
                    Log.d("seek to", ""+Long.parseLong(frameTimesDemo[stepNum-1])/1000);
                    exoPlayerDemo.seekTo(Long.parseLong(frameTimesDemo[stepNum-1])/1000);
                }
            }
        });

        rightdemoButton.setOnClickListener(v->{
            if (stepNum<12){
                stepNum++;
                instructionsDemo.setText("Select Step "+stepNum);
                if (frameTimesDemo[stepNum-1] != null){
                    Log.d("seek to", ""+Long.parseLong(frameTimesDemo[stepNum-1])/1000);
                    exoPlayerDemo.seekTo(Long.parseLong(frameTimesDemo[stepNum-1])/1000);

                }
            }
        });

        demoSelect.setOnClickListener(v->{
            String frameTime = String.valueOf(exoPlayerDemo.getCurrentPosition() * 1000); // Convert to microseconds
            frameTimesDemo[stepNum-1] = frameTime;
            Log.d("set step","Step "+stepNum+" : "+frameTime);
        });

        demoDelete.setOnClickListener(v->{
            frameTimesDemo[stepNum-1] = null;
            Log.d("set step","Step "+stepNum+" : removed");

        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayerVideo != null) {
            exoPlayerVideo.setPlayWhenReady(false); // Pause playback
        }
        if (exoPlayerDemo != null) {
            exoPlayerDemo.setPlayWhenReady(false); // Pause playback
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayerVideo != null) {
            exoPlayerVideo.release(); // Release player resources
        }
        if (exoPlayerDemo != null) {
            exoPlayerDemo.release(); // Release player resources
        }
    }

}
