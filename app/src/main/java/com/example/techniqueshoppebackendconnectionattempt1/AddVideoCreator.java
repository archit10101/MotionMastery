package com.example.techniqueshoppebackendconnectionattempt1;

import android.Manifest;
import androidx.lifecycle.LifecycleOwner;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        checkStoragePermissions();
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

            if (imageFile == null){
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



            rdbc.uploadFileNew(AddVideoCreator.this,urlPresigned, vidfile);


            rdbc.uploadFileNew(AddVideoCreator.this, demoUrlPresigned, demoFile);


            rdbc.uploadFileNew(AddVideoCreator.this, thumbnailurlPresigned, imageFile);






        });
    }

    public static File getFileFromUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String fileName = getFileNameFromUri(contentResolver, uri);
        File tempFile = new File(context.getCacheDir(), fileName);
        try (InputStream inputStream = contentResolver.openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private static String getFileNameFromUri(ContentResolver contentResolver, Uri uri) {
        String result = null;
        try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
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
        }if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
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
                        videoUri = (uuid);

                        startVideoInstructions();


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
                String result = data.getStringExtra("RESULT");
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
            } catch (Exception ignored) {

            }
        }
    }
    public static File convertContentUriToFile(Context context, Uri contentUri) {
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String fileName = getFileName(contentResolver, contentUri);
            File outputDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            file = new File(outputDir, fileName);
            inputStream = contentResolver.openInputStream(contentUri);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // Adjust buffer size as needed
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IO exception
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle IO exception
            }
        }
        return null;
    }

    private static String getFileName(ContentResolver contentResolver, Uri uri) {
        String fileName = null;
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex != -1) {
                    fileName = cursor.getString(displayNameIndex);
                } else {
                    fileName = "Unknown";
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return fileName;
    }

    private String thumbnailuuid;

    private String thumbnailurlPresigned;
    private File imageFile;
    private File vidfile;
    private File demoFile;
    public void startVideoInstructions(){
        exoPlayerVideo = new ExoPlayer.Builder(AddVideoCreator.this).build();
        Log.d("uri","vidUri: "+vidUri+" videoUri: "+videoUri);
        vidfile = convertContentUriToFile(this,vidUri);


        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(vidfile));
        exoPlayerVideo.setMediaItem(mediaItem);
        exoPlayerVideo.prepare();
        exoPlayerVideo.play();
        vidPlayerView.setPlayer(exoPlayerVideo);

        vidPlayerView.setVisibility(View.VISIBLE);
        vidController.setVisibility(View.VISIBLE);
        instructionsVid.setVisibility(View.VISIBLE);

        instructionsVid.setText("Select video thumbnail.");

        vidDelete.setOnClickListener(v->{
            thumbnailuuid = null;
            thumbnailurlPresigned = null;
        });

        vidSelect.setOnClickListener(v ->{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(AddVideoCreator.this, vidUri);
            String frameTime = String.valueOf(exoPlayerVideo.getCurrentPosition() * 1000); // Convert to microseconds
            Bitmap frameBitmap = retriever.getFrameAtTime(Long.parseLong(frameTime));
            if (frameBitmap != null) {
                // Create a temporary file
                try {
                    imageFile = File.createTempFile("frame", ".png", getCacheDir());
                    // Create a FileOutputStream to write the bitmap to the temporary file
                    FileOutputStream outputStream = new FileOutputStream(imageFile);

                    // Compress and write the bitmap to the FileOutputStream
                    frameBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                    // Close the FileOutputStream
                    outputStream.close();

                    // Now you have the bitmap saved as a temporary file
                    // Use tempFile as needed

                    // Delete the temporary file when no longer needed
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle error
                }
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // Check if we have permission to read external storage
    private void checkStoragePermissions() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d("permissins","no");
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            Log.d("permissins","yes");
        }
    }

    // Handle the permission request response



    private int stepNum;

    private String[] frameTimesDemo = new String[12];

    private void startDemoInstructions(){
        exoPlayerDemo = new ExoPlayer.Builder(AddVideoCreator.this).build();
        demoFile = convertContentUriToFile(this,demoUri);


        MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(demoFile));

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

        if (imageFile !=null){
            imageFile.delete();

        }if (vidfile !=null){
            vidfile.delete();

        }if (demoFile !=null){
            demoFile.delete();

        }
    }

}
