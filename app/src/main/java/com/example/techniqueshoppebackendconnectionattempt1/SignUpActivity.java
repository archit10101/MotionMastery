package com.example.techniqueshoppebackendconnectionattempt1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    RetrofitDBConnector rdbc;

    private UserInfoSingleton userInfoSingleton;

    TextInputEditText firstnameBox,lastnameBox,usernameBox,passwordBox,emailBox;

    Button signup, login;

    TextView welcomeText,subText,warningText;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstnameBox = findViewById(R.id.firstNameBox);
        lastnameBox = findViewById(R.id.lastNameBox);
        usernameBox = findViewById(R.id.userName);
        passwordBox = findViewById(R.id.password);
        emailBox = findViewById(R.id.emailBox);
        signup = findViewById(R.id.SignUpButton);
        login = findViewById(R.id.loginButton);
        welcomeText = findViewById(R.id.welcomeText);
        subText = findViewById(R.id.signupCaption);
        logo = findViewById(R.id.logoImage);
        warningText = findViewById(R.id.warningText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(usernameBox,"userNameTransition");
                pairs[1] = new Pair<View,String>(passwordBox,"passwordTransition");
                pairs[2] = new Pair<View,String>(welcomeText,"logoTxt");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
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

        rdbc = new RetrofitDBConnector();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.MyData userInfo = new UserInfo.MyData(0,usernameBox.getText().toString(),passwordBox.getText().toString(),emailBox.getText().toString(),firstnameBox.getText().toString(),lastnameBox.getText().toString(),"unknown");

                rdbc.postNewUser(userInfo, new LoginActivity.UserCallback() {
                    @Override
                    public void onSuccess(List<UserInfo.MyData> userData) {
                        userInfoSingleton = UserInfoSingleton.getInstance(userData);
                        Intent intent = new Intent(SignUpActivity.this,AppActivity.class);
                        startActivity(intent);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        warningText.setVisibility(View.VISIBLE);
                        warningText.setText(errorMessage);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                warningText.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                });
            }
        });
    }
}