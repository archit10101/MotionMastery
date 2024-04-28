package com.example.techniqueshoppebackendconnectionattempt1;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LaunchActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 10001;
    Animation topAnimation,bottomAnimation;
    ImageView logo;

    TextView title;

    Button login, signup;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animator);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animator);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with image selection
        }

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.Title);
        login = findViewById(R.id.LoginButton);
        signup = findViewById(R.id.SignUpButton);

        logo.setAnimation(topAnimation);
        title.setAnimation(bottomAnimation);
        login.setAnimation(bottomAnimation);
        signup.setAnimation(bottomAnimation);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(logo,"logoImg");
                pairs[1] = new Pair<View,String>(title,"logoTxt");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LaunchActivity.this,pairs);
                startActivity(intent,options.toBundle());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this,SignUpActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(logo,"logoImg");
                pairs[1] = new Pair<View,String>(title,"logoTxt");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LaunchActivity.this,pairs);
                startActivity(intent,options.toBundle());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Toast.makeText(this, "Permission Agreed! "  + ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES), Toast.LENGTH_SHORT).show();

            } else {
                int apiLevel = Build.VERSION.SDK_INT;

                Toast.makeText(this, "Permission Denied! "  + apiLevel, Toast.LENGTH_SHORT).show();
            }
        }
    }
}