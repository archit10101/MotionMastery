package com.example.techniqueshoppebackendconnectionattempt1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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

public class LaunchActivity extends AppCompatActivity {

    Animation topAnimation,bottomAnimation;
    ImageView logo;

    TextView title;

    Button login, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animator);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animator);

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
}