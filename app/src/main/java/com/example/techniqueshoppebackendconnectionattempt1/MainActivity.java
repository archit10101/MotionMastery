package com.example.techniqueshoppebackendconnectionattempt1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    private String url = "http://techniqueapp-env.eba-v6a3p9mb.us-east-2.elasticbeanstalk.com/users/arc_t";

    private TextView text;
    private Button get;

    interface RequestUser{
        @GET("/users/{user_name}")
        Call<user_info> getUser(@Path("user_name") String user_name);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = findViewById(R.id.button);
        text = findViewById(R.id.textView3);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://techniqueshoppe-env.eba-pmmmtci6.us-east-2.elasticbeanstalk.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
