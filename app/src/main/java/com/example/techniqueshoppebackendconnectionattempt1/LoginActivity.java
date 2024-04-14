package com.example.techniqueshoppebackendconnectionattempt1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    ImageView logoImg;

    TextView welcome, signinText, warningText;

    TextInputEditText usernameBox,passwordBox;

    Button loginButton, signupButton;

    RetrofitDBConnector rdbc;

    private UserInfoSingleton userInfoSingleton;

    public interface UserCallback {
        void onSuccess(List<UserInfo.MyData> userData);
        void onFailure(String errorMessage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED,WindowManager.LayoutParams.FLAGS_CHANGED);
        logoImg = findViewById(R.id.logoImage);
        welcome = findViewById(R.id.welcomeText);
        signinText = findViewById(R.id.signInCaption);
        warningText = findViewById(R.id.wrongPassword);
        usernameBox = findViewById(R.id.usernameBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.SignUpButton);

        rdbc = new RetrofitDBConnector();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(usernameBox,"userNameTransition");
                pairs[1] = new Pair<View,String>(passwordBox,"passwordTransition");
                pairs[2] = new Pair<View,String>(welcome,"logoTxt");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("userName",usernameBox.getText().toString());
                Log.d("passWord",passwordBox.getText().toString());
                rdbc.getUserData(usernameBox.getText().toString(),passwordBox.getText().toString(), new UserCallback() {
                    @Override
                    public void onSuccess(List<UserInfo.MyData> userData) {
                        userInfoSingleton = UserInfoSingleton.getInstance(userData);
                        Log.d("first",userInfoSingleton.getDataList().get(0).getFirstName());
                        Log.d("last",userInfoSingleton.getDataList().get(0).getLastName());

                        Toast.makeText(LoginActivity.this,userInfoSingleton.getDataList().get(0).getFirstName()+" "+userInfoSingleton.getDataList().get(0).getLastName(),Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        warningText.setVisibility(View.VISIBLE);
                        usernameBox.setText("");
                        passwordBox.setText("");
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