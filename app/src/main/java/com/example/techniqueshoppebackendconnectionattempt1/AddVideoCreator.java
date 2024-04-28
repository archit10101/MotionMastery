package com.example.techniqueshoppebackendconnectionattempt1;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.PresignedUrlResponse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.VideoTutorial;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AddVideoCreator extends AppCompatActivity {

    private ImageView vid_close;
    private TextInputEditText editVidName;
    private TextInputEditText editVidDescription;
    private MaterialButton buttonUploadVideo;
    private MaterialButton buttonCreateVideo;
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
        rdbc = new RetrofitDBConnector();

        videoUri = "blank";
        vid_close.setOnClickListener(v -> {
            finish();
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
                Toast.makeText(this, "Please enter vidURI description", Toast.LENGTH_SHORT).show();
                return;
            }
            VideoTutorial videoTutorial = new VideoTutorial(0,Integer.parseInt(courseId),videoName,videoDescription,"imgpath",videoUri,4);
            rdbc.createVideoTutorial(videoTutorial, new RetrofitDBConnector.VideoCallback() {
                @Override
                public void onSuccess(List<VideoTutorial> fileContent) {
                    Toast.makeText(AddVideoCreator.this,"Success: "+fileContent.get(0).getVideoName(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(AddVideoCreator.this,"Failure: "+error,Toast.LENGTH_SHORT).show();

                }
            });



        });
    }
    private void openImagePicker() {
        Log.d("I am here","This is openImagePicker");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK )
        {
            try {
                Log.d("here","here");
                Uri imageUri = data.getData();



                rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                    @Override
                    public void onUploadSuccess(PresignedUrlResponse uploadObject) {
                        uuid = uploadObject.getUuid();
                        urlPresigned = uploadObject.getUrl();
                        Log.d("uuid",uuid);
                        Log.d("urlPresigned",urlPresigned);
                        ContentResolver contentResolver = getApplication().getContentResolver();
                        try (InputStream inputStream = contentResolver.openInputStream(imageUri)) {
                            if (inputStream == null){
                                Log.d("inputStream","input stream is null");

                            }else{
                                rdbc.uploadWithURI(inputStream, urlPresigned, "video/*");
                            }
                        } catch (IOException e) {
                            // Handle potential exceptions during stream opening
                            e.printStackTrace();
                        }
                        videoUri = (uuid);

                    }

                    @Override
                    public void onUploadFailure(String error) {
                        Log.d("imageError",error);
                    }

                });
            } catch (Exception e) {

            }
        }
    }

}
